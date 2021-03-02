package com.antoine.alphatheorytakehome.model;

import javax.persistence.*;

@Entity
@Table(name = "Funds")
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private DataSet dataSet;

    private String name;

    public Fund() {}

    public Fund(String name, DataSet dataset) {
        this.name = name;
        this.dataSet = dataset;
    }

    public String getName() {
        return name;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public Integer getId() {
        return id;
    }
}