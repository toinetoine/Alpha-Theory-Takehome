package com.antoine.alphatheorytakehome.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TickerSnapshots")
public class TickerSnapshot implements Snapshot<Ticker, Object> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private DataSet dataSet;

    @ManyToOne
    private Ticker ticker;

    private Date date;

    private Float value;

    private Float gainLossVersusEarliest;

    private Float gainLossVersusLatest;

    public TickerSnapshot() {
    }

    public TickerSnapshot(Ticker ticker, Date date, float value, DataSet dataSet) {
        this.ticker = ticker;
        this.date = date;
        this.value = value;
        this.dataSet = dataSet;
    }

    public void addValue(Float value) {
        this.value += value;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Float getGainLossVersusEarliest() {
        return gainLossVersusEarliest;
    }

    public Float getGainLossVersusLatest() {
        return gainLossVersusLatest;
    }


    public void setGainLossVersusEarliest(Float gainLossVersusEarliest) {
        this.gainLossVersusEarliest = gainLossVersusEarliest;
    }

    public void setGainLossVersusLatest(Float gainLossVersusLatest) {
        this.gainLossVersusLatest = gainLossVersusLatest;
    }

    public Ticker getFirstUnderlying() {
        return this.ticker;
    }

    public Object getSecondUnderlying() {
        return null;
    }

    public Float getValue() {
        return value;
    }

    public DataSet getDataSet() {
        return dataSet;
    }
}
