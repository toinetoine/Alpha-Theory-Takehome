package com.antoine.alphatheorytakehome.controller;

import com.antoine.alphatheorytakehome.dal.MarketValueRepository;
import com.antoine.alphatheorytakehome.model.MarketValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/marketvalue")
public class MarketValueController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final MarketValueRepository marketValueRepository;

    public MarketValueController(MarketValueRepository marketValueRepository) {
        this.marketValueRepository = marketValueRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<MarketValue> getAllMarketValues(HttpServletRequest request) {
        LOG.info("{} getting all market values.", request.getRemoteAddr());
        return marketValueRepository.findAll();
    }
}
