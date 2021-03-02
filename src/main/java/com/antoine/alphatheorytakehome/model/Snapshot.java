package com.antoine.alphatheorytakehome.model;

import java.util.Date;

public interface Snapshot<T1, T2> {
    T1 getFirstUnderlying();
    T2 getSecondUnderlying();
    Float getValue();
    void setGainLossVersusEarliest(Float value);
    void setGainLossVersusLatest(Float value);
    void addValue(Float value);
    Date getDate();
}
