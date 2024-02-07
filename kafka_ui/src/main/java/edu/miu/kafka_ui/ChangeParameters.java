package edu.miu.kafka_ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChangeParameters implements Runnable {
    int windowSize = 20;
    int spikeRate = 10;
    int dataLowerBound = -5;
    int dataUpperBound = 5;
    int numberOfDataPoints = 8;

    List<Float> startingPoints = new ArrayList<>();

    long ticker = 0;
    private static final long TICKER_SLEEP_TIME = 1000;

    public static final ChangeParameters instance = new ChangeParameters();

    private ChangeParameters() {
        this.populateStartingPoint();

        new Thread(instance).start();
    }

    private void populateStartingPoint() {
        Random rand = new Random();
        for (int i = 0; i < numberOfDataPoints; i++) {
            startingPoints.add(rand.nextFloat());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ticker += 1;
                Thread.sleep(TICKER_SLEEP_TIME);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getWindowSize() {
        return this.windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getSpikeRate() {
        return this.spikeRate;
    }

    public void setSpikeRate(int spikeRate) {
        this.spikeRate = spikeRate;
    }

    public int getDataLowerBound() {
        return this.dataLowerBound;
    }

    public void setDataLowerBound(int dataLowerBound) {
        this.dataLowerBound = dataLowerBound;
    }

    public int getDataUpperBound() {
        return this.dataUpperBound;
    }

    public void setDataUpperBound(int dataUpperBound) {
        this.dataUpperBound = dataUpperBound;
    }

    public int getNumberOfDataPoints() {
        return this.numberOfDataPoints;
    }

    public void setNumberOfDataPoints(int numberOfDataPoints) {
        this.numberOfDataPoints = numberOfDataPoints;
        startingPoints.clear();
        this.populateStartingPoint();
    }

    public List<Float> getStartingPoints() {
        return this.startingPoints;
    }

    public void setStartingPoints(List<Float> startingPoints) {
        this.startingPoints = startingPoints;
    }

    public long getTicker() {
        return this.ticker;
    }

    public void setTicker(long ticker) {
        this.ticker = ticker;
    }
}
