package com.StockPredictor.Indicators;

import java.util.List;
import java.util.LinkedList;

/**
 * Calculates the Simple Moving average for a given window
 */
public class SMA {

    private final int windowSize;

    private final List<Double> dataWindowCloses;

    private double simpleMovingAverage = 0.0;

    private int processedPeriods = 0;

    private double runningSum;

    /**
     * Create the Simple Moving Average
     * @param windowSize simple moving average window
     */
    public SMA(int windowSize) {

        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be positive");
        }

        this.dataWindowCloses = new LinkedList<>();

        this.windowSize = windowSize;

    }

    /**
     * Set the simple moving average
     * @param candleClose the last candles close price
     */
    public void setSMA(double candleClose) {
        addCloses(candleClose);
        calculateSMA();
        processedPeriods++;
    }

    /**
     * Maintain Close prices set to window size
     * @param candleClose the last candles close price
     */
    private void addCloses(double candleClose) {

        if (dataWindowCloses.size() == windowSize) {
            runningSum -= dataWindowCloses.remove(0);
        }

        dataWindowCloses.add(candleClose);
        runningSum += candleClose;

    }

    /**
     * Calculates the simple moving average
     * -- Average of the past candle closes given a window size
     */
    private void calculateSMA() {
        if (dataWindowCloses.size() == windowSize) {
            simpleMovingAverage = runningSum / windowSize;
        }
    }

    public boolean isReady(){
        return processedPeriods >= windowSize;
    }

    public double getSMA() {
        return simpleMovingAverage;
    }
}
