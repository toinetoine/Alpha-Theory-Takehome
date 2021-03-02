package com.antoine.alphatheorytakehome.controller;

import com.antoine.alphatheorytakehome.dal.DataSetRepository;
import com.antoine.alphatheorytakehome.dal.MarketValueDal;
import com.antoine.alphatheorytakehome.model.DataSet;
import com.antoine.alphatheorytakehome.model.Fund;
import com.antoine.alphatheorytakehome.model.MarketValue;
import com.antoine.alphatheorytakehome.model.Ticker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping(value = "/dataset")
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET})
public class DatasetController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final DataSetRepository dataSetRepository;
    private final MarketValueDal marketValueDal;

    private final String[] REQUIRED_CSV_FIELDS = {
            "Portfolio",
            "Ticker",
            "Date",
            "Market Value"
    };
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public DatasetController(DataSetRepository dataSetRepository, MarketValueDal marketValueDal) {
        this.dataSetRepository = dataSetRepository;
        this.marketValueDal = marketValueDal;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<DataSet> getAllDataSets(HttpServletRequest request) {
        LOG.info("{} getting all datasets.", request.getRemoteAddr());
        return dataSetRepository.findAll();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public DataSet uploadDataSet(
            @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        LOG.info("Uploading file {} {}KB from {}", file.getName(), file.getSize()/1000, request.getRemoteAddr());
        Map<String, Integer> columnHeaders = new HashMap<>();
        List<CSVRecord> csvRecords;
        try {
            BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            csvRecords = csvParser.getRecords();
            CSVRecord headerRow = csvRecords.remove(0);
            for (int i = 0; i < headerRow.size(); i ++) {
                columnHeaders.put(headerRow.get(i), i);
            }
        } catch (Exception e) {
            LOG.error("Error while parsing dataset file {} from <{}>: {}",file.getName(),
                    request.getRemoteAddr(), e.toString());
            throw new Error("Error reading uploaded file");
        }

        // Ensure required column headers are present
        Arrays.stream(REQUIRED_CSV_FIELDS).forEach((field) -> {
            if (!columnHeaders.containsKey(field)) {
                throw new Error("Uploaded CSV file missing column header '" + field + "' (case-sensitive)");
            }
        });

        DataSet dataSet = new DataSet(file.getName(), request.getRemoteAddr());
        Map<String, Fund> funds = new HashMap<>();
        Map<String, Ticker> tickers = new HashMap<>();
        List<MarketValue> marketValues = new ArrayList<>();

        for (CSVRecord record : csvRecords) {
            // Ensure number of columns equals number of column headers
            if (record.size() != columnHeaders.size()) {
                throw new Error("Abort: Number of columns doesn't equal the number of column headers (" +
                        columnHeaders.size() + ") " + record.toString());
            }

            String fundName = record.get(columnHeaders.get("Portfolio"));
            Fund fund = funds.get(fundName);
            if (fund == null) {
                fund = new Fund(fundName, dataSet);
                funds.put(fundName, fund);
            }

            String tickerName = record.get(columnHeaders.get("Ticker"));
            Ticker ticker = tickers.get(tickerName);
            if (ticker == null) {
                ticker = new Ticker(tickerName, dataSet);
                tickers.put(tickerName, ticker);
            }

            float marketValue;
            try {
                marketValue = Float.parseFloat(record.get(columnHeaders.get("Market Value")));
            } catch (NumberFormatException e) {
                throw new Error("Abort: Unable to parse " + record.get(columnHeaders.get("Market Value") +
                        " as a number."));
            }

            Date date;
            try {
                date = DATE_FORMAT.parse(record.get(columnHeaders.get("Date")));
            } catch (ParseException e) {
                throw new Error("Abort: Unable to parse date " + record.get(columnHeaders.get("Date") +
                        ". Must be in: " + DATE_FORMAT.toPattern()));
            }

            marketValues.add(new MarketValue(ticker, fund, date, marketValue, dataSet));
        }

        DataSet savedDataSet = dataSetRepository.save(dataSet);
        marketValueDal.addMarketValues(marketValues, tickers.values(), funds.values());

        return savedDataSet;
    }
}
