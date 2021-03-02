package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.DataSet;
import org.springframework.data.repository.CrudRepository;
import com.antoine.alphatheorytakehome.model.Fund;

import java.util.List;

public interface FundRepository extends CrudRepository<Fund, Integer>{

    List<Fund> findByDataSet(DataSet dataSetId);
}