package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.Ticker;
import org.springframework.data.repository.CrudRepository;

public interface TickerRepository extends CrudRepository<Ticker, Integer> {
}
