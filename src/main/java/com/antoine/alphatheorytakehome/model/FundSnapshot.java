package com.antoine.alphatheorytakehome.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FundSnapshots")
public class FundSnapshot implements Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private DataSet dataSet;

    @ManyToOne
    private Fund fund;

    private Date date;

    private Float value;

    private Float gainLossVersusEarliest;

    private Float gainLossVersusLatest;

    public FundSnapshot() {
    }

    public FundSnapshot(Fund fund, Date date, float value, DataSet dataSet) {
        this.fund = fund;
        this.date = date;
        this.value = value;
        this.dataSet = dataSet;
    }

    public void addValue(Float value) {
        this.value += value;
    }

    public Fund getFund() {
        return fund;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Float getGainLossVersusLatest() {
        return gainLossVersusLatest;
    }

    public Float getGainLossVersusEarliest() {
        return gainLossVersusEarliest;
    }

    public void setGainLossVersusEarliest(Float gainLossVersusEarliest) {
        this.gainLossVersusEarliest = gainLossVersusEarliest;
    }

    public void setGainLossVersusLatest(Float gainLossVersusLatest) {
        this.gainLossVersusLatest = gainLossVersusLatest;
    }

    public Fund getFirstUnderlying() {
        return this.fund;
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
