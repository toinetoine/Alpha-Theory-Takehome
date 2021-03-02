package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.DataSet;
import org.springframework.data.repository.CrudRepository;

public interface DataSetRepository  extends CrudRepository<DataSet, Integer> {
}
