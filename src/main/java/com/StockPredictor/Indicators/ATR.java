package com.StockPredictor.Indicators;

import java.util.List;
import java.util.LinkedList;
import java.lang.Math;
import java.util.OptionalDouble;

public class ATR {

    private final int windowSize;
    private final List<Double> dataWindowHighs;
    private final List<Double> dataWindowLows;
    private final List<Double> dataWindowTrueRanges;
    private double averageTrueRange = 0.0;

    private double previousClose;

    /**
     * Average True Range Indicator to determine price range and potential movement range
     *
     * @param windowSize window you ATR should be calculated for
     */
    public ATR(int windowSize){

        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be positive");
        }

        this.dataWindowHighs = new LinkedList<>();
        this.dataWindowLows = new LinkedList<>();
        this.dataWindowTrueRanges = new LinkedList<>();

        this.windowSize = windowSize;

    }

    /**
     * Sets the value of the ATR safely
     * @param candleHigh current candles high
     * @param candleLow current candles low
     * @param previousClose previous candles close
     */
    public void setATR(double candleHigh, double candleLow, double previousClose) {
        this.previousClose = previousClose;
        addHighs(candleHigh);
        addLows(candleLow);

        addTrueRange(candleHigh, candleLow);

        calculateATR();
    }

    /**
     * Adds the High of the latest candle to the dataWindowHighs CLL
     * @param candleHigh High of the latest Candle
     */
    private void addHighs(double candleHigh) {
        if (dataWindowHighs.size() == windowSize) {
            ((LinkedList<Double>) dataWindowHighs).removeFirst();
        }

        dataWindowHighs.add(candleHigh);
    }

    /**
     * Adds the Low of the latest candle to the dataWindowLows CLL
     * @param candleLow Low of the latest Candle
     */
    private void addLows(double candleLow) {
        if (dataWindowLows.size() == windowSize) {
            ((LinkedList<Double>) dataWindowLows).removeFirst();
        }

        dataWindowLows.add(candleLow);
    }

    /**
     * Set the True Range Data Window CLL
     * Def - The Highest of the three possibilities below
     * @param candleHigh High of the current candle
     * @param candleLow Low of the current candle
     */
    private void addTrueRange(double candleHigh, double candleLow) {
        double currentHighMinusLow = candleHigh - candleLow;
        double currentHighMinusPreviousClose = Math.abs(candleHigh - previousClose);
        double currentLowMinusPreviousClose = Math.abs(candleLow - previousClose);

        if (dataWindowTrueRanges.size() == windowSize) {
            ((LinkedList<Double>) dataWindowTrueRanges).removeFirst();
        }

        dataWindowTrueRanges.add(Math.max(currentHighMinusLow, Math.max(currentHighMinusPreviousClose, currentLowMinusPreviousClose)));
    }

    /**
     * Calculates the Average True Range Value Based on the Formula
     */
    private void calculateATR() {
        if (averageTrueRange == 0.0) {
            if (dataWindowTrueRanges.size() == windowSize) {
                OptionalDouble average = dataWindowTrueRanges.stream().mapToDouble(d -> d).average();
                averageTrueRange = average.orElse(0.0);
            }
        } else {
            averageTrueRange = ((averageTrueRange * (windowSize - 1)) + dataWindowTrueRanges.get(windowSize - 1)) / windowSize;
        }
    }

    public double getAverageTrueRange() {
        return averageTrueRange;
    }

}
