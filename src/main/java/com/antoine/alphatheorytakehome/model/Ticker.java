package com.antoine.alphatheorytakehome.model;

import javax.persistence.*;

@Entity
@Table(name = "Tickers")
public class Ticker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private DataSet dataSet;

    private String name;

    public Ticker() {}

    public Ticker(String name, DataSet dataset) {
        this.name = name;
        this.dataSet = dataset;
    }

    public Integer getId() {
        return id;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public String getName() {
        return name;
    }
}