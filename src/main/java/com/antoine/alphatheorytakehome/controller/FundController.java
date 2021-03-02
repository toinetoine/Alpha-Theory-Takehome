package com.antoine.alphatheorytakehome.controller;

import com.antoine.alphatheorytakehome.dal.DataSetRepository;
import com.antoine.alphatheorytakehome.dal.FundRepository;
import com.antoine.alphatheorytakehome.model.DataSet;
import com.antoine.alphatheorytakehome.model.Fund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/fund")
public class FundController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final FundRepository fundRepository;
    private final DataSetRepository dataSetRepository;

    public FundController(FundRepository fundRepository, DataSetRepository dataSetRepository) {
        this.fundRepository = fundRepository;
        this.dataSetRepository = dataSetRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<Fund> getAllFunds(@RequestParam("dataset") Integer datasetId,
                                      HttpServletRequest request) {
        LOG.info("{} getting all funds.", request.getRemoteAddr());
        DataSet dataSet = dataSetRepository.findById(datasetId).orElse(null);
        if (dataSet == null) {
            throw new Error("No matching Dataset with id " + datasetId);
        }
        return fundRepository.findByDataSet(dataSet);
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public Fund getSingle(@RequestParam("id") Integer fundId,
                          HttpServletRequest request) {
        System.out.println(fundId);
        LOG.info("{} getting all funds.", request.getRemoteAddr());
        return fundRepository.findById(fundId).orElse(null);
    }
}
