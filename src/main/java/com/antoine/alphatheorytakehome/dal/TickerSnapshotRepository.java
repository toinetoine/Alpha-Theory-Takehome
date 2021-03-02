package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.TickerSnapshot;
import org.springframework.data.repository.CrudRepository;

public interface TickerSnapshotRepository  extends CrudRepository<TickerSnapshot, Integer> {
}
