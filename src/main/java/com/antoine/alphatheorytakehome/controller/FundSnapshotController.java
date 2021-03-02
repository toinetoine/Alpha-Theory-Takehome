package com.antoine.alphatheorytakehome.controller;

import com.antoine.alphatheorytakehome.dal.DataSetRepository;
import com.antoine.alphatheorytakehome.dal.FundRepository;
import com.antoine.alphatheorytakehome.dal.FundSnapshotRepository;
import com.antoine.alphatheorytakehome.model.Fund;
import com.antoine.alphatheorytakehome.model.FundSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
    @RequestMapping(value = "/fundsnapshot")
public class FundSnapshotController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final FundRepository fundRepository;
    private final FundSnapshotRepository fundSnapshotRepository;

    public FundSnapshotController(FundRepository fundRepository, FundSnapshotRepository fundSnapshotRepository) {
        this.fundRepository = fundRepository;
        this.fundSnapshotRepository = fundSnapshotRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<FundSnapshot> getAllSnapshots(@RequestParam("fund") Integer fundId,
                                                  HttpServletRequest request) {
        LOG.info("{} getting all fundsnapshots for fund {}.", request.getRemoteAddr(), fundId);
        Fund fund = fundRepository.findById(fundId).orElse(null);
        if (fund == null) {
            throw new Error("No matching Fund with id " + fundId);
        }

        return fundSnapshotRepository.findByFund(fund).stream().sorted(
                Comparator.comparing(FundSnapshot::getDate)).collect(Collectors.toList());
    }
}
