package com.antoine.alphatheorytakehome.dal;

import com.antoine.alphatheorytakehome.model.*;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MarketValueDal {
    private final MarketValueRepository marketValueRepository;
    private final TickerRepository tickerRepository;
    private final FundRepository fundRepository;
    private final FundSnapshotRepository fundSnapshotRepository;
    private final TickerSnapshotRepository tickerSnapshotRepository;

    public MarketValueDal(MarketValueRepository marketValueRepository,
                          FundRepository fundRepository,
                          TickerRepository tickerRepository,
                          FundSnapshotRepository fundSnapshotRepository,
                          TickerSnapshotRepository tickerSnapshotRepository) {
        this.marketValueRepository = marketValueRepository;
        this.fundRepository = fundRepository;
        this.tickerRepository = tickerRepository;
        this.fundSnapshotRepository = fundSnapshotRepository;
        this.tickerSnapshotRepository = tickerSnapshotRepository;
    }

    public void addMarketValues(Collection<MarketValue> marketValues,
                                Collection<Ticker> tickers,
                                Collection<Fund> funds) {
        fundRepository.saveAll(funds);
        tickerRepository.saveAll(tickers);

        List<MarketValue> sortedMarketValues = marketValues.stream().sorted(
                Comparator.comparing(MarketValue::getDate)).collect(Collectors.toList());
        rollUp(sortedMarketValues);
        marketValueRepository.saveAll(marketValues);

        HashMap<Integer, TickerSnapshot> tickerSnapshots = new HashMap<>();
        HashMap<Integer, FundSnapshot> fundSnapshots = new HashMap<>();
        marketValues.forEach((marketValue) -> {
            // Create (or add to) TickerSnapshot
            int dateTickerHash = Objects.hash(marketValue.getTicker(), marketValue.getDate());
            TickerSnapshot tickerSnapshot = tickerSnapshots.get(dateTickerHash);
            if (tickerSnapshot == null) {
                tickerSnapshot = new TickerSnapshot(marketValue.getTicker(), marketValue.getDate(),
                        marketValue.getValue(), marketValue.getDataSet());
                tickerSnapshots.put(dateTickerHash, tickerSnapshot);
            } else {
                tickerSnapshot.addValue(marketValue.getValue());
            }

            // Create (or add to) FundSnapshot
            int dateFundHash = Objects.hash(marketValue.getFund(), marketValue.getDate());
            FundSnapshot fundSnapshot = fundSnapshots.get(dateFundHash);
            if (fundSnapshot == null) {
                fundSnapshot = new FundSnapshot(marketValue.getFund(), marketValue.getDate(),
                        marketValue.getValue(), marketValue.getDataSet());
                fundSnapshots.put(dateFundHash, fundSnapshot);
            } else {
                fundSnapshot.addValue(marketValue.getValue());
            }
        });

        List<Snapshot> sortedFundSnapshots = fundSnapshots.values().stream().sorted(
                Comparator.comparing(Snapshot::getDate)).collect(Collectors.toList());
        rollUp(sortedFundSnapshots);
        List<Snapshot> sortedTickerSnapshots = tickerSnapshots.values().stream().sorted(
                Comparator.comparing(Snapshot::getDate)).collect(Collectors.toList());
        rollUp(sortedTickerSnapshots);

        fundSnapshotRepository.saveAll(fundSnapshots.values());
        tickerSnapshotRepository.saveAll(tickerSnapshots.values());
    }

    private void rollSingle(Snapshot snapshot, Map<Integer, Snapshot> firstSnapshots, boolean isFoward) {
        int hash = Objects.hash(snapshot.getFirstUnderlying(), snapshot.getSecondUnderlying());
        Snapshot firstSnapshot = firstSnapshots.get(hash);
        if (firstSnapshot == null) {
            firstSnapshots.put(hash, snapshot);
        } else {
            if (isFoward) {
                snapshot.setGainLossVersusEarliest(
                        (snapshot.getValue() - firstSnapshot.getValue()) / firstSnapshot.getValue());
            } else {
                snapshot.setGainLossVersusLatest(
                        (firstSnapshot.getValue() - snapshot.getValue()) / snapshot.getValue());
            }
        }
    }

    private void rollUp(List<? extends Snapshot> snapshots) {
        Map<Integer, Snapshot> firstSnapshots = new HashMap<>();
        // roll forwards
        for (Snapshot snapshot : snapshots) {
            rollSingle(snapshot, firstSnapshots, true);
        }
        firstSnapshots.clear(); // reuse
        // roll backwards
        for (int j = snapshots.size() - 1; j >= 0; j--) {
            rollSingle(snapshots.get(j), firstSnapshots, false);
        }
    }
}
