package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.Fund;
import com.antoine.alphatheorytakehome.model.FundSnapshot;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FundSnapshotRepository extends CrudRepository<FundSnapshot, Integer> {
    List<FundSnapshot> findByFund(Fund fund);
}
