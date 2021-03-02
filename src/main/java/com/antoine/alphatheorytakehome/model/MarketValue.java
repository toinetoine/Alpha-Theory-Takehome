package com.antoine.alphatheorytakehome.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MarketValues")
public class MarketValue implements Snapshot<Fund, Ticker> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Date date;

    @ManyToOne
    private Ticker ticker;

    @ManyToOne
    private Fund fund;

    @ManyToOne
    private DataSet dataSet;

    private float value;

    private float gainLossVersusEarliest;

    private float gainLossVersusLatest;

    public MarketValue() {}

    public MarketValue(Ticker ticker, Fund fund, Date date, float value, DataSet dataset) {
        this.ticker = ticker;
        this.fund = fund;
        this.date = date;
        this.value = value;
        this.dataSet = dataset;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Fund getFund() {
        return fund;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public Fund getFirstUnderlying() {
        return this.fund;
    }

    public Ticker getSecondUnderlying() {
        return this.ticker;
    }

    public Float getValue() {
        return value;
    }

    public void setGainLossVersusEarliest(Float gainLossSinceFirst) {
        this.gainLossVersusEarliest = gainLossSinceFirst;
    }

    public float getGainLossVersusEarliest() {
        return gainLossVersusEarliest;
    }


    public void setGainLossVersusLatest(Float gainLossSinceLatest) {
        this.gainLossVersusLatest = gainLossSinceLatest;
    }

    public void addValue(Float value) {
        this.value = value;
    }

    public float getGainLossVersusLatest() {
        return gainLossVersusLatest;
    }
}
