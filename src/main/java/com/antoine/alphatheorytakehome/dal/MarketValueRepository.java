package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.MarketValue;
import org.springframework.data.repository.CrudRepository;

public interface MarketValueRepository extends CrudRepository<MarketValue, Integer> {
}
