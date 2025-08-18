package com.StockPredictor.Indicators;

import java.util.List;
import java.lang.Math;
import java.util.LinkedList;

/**
 * Gives the upper and lower bollinger bands for a window
 */
public class BollingerBands {
    private final StandardDeviationCalculator SDC;

    private final SMA simpleMovingAverage;

    private final int windowSize;
    private final double standardDeviation;
    private final List<Double> dataWindowCloses;

    private int periodsProcessed = 0;
    private double upperBand = 0.0;
    private double lowerBand = 0.0;

    // TODO: Allow for population (N) and sample (N-1) variances

    /**
     * Setup the Bollinger Band technical Indicator
     * @param windowSize the number of candles to calculate the BB from
     * @param standardDeviation The number of standard deviations to be factored into the BB calculation
     */
    public BollingerBands(int windowSize, double standardDeviation) {

        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window Size must be greater than zero");
        } else if (standardDeviation <= 0) {
            throw new IllegalArgumentException("standard deviation must be greater than zero");
        }

        SDC = new StandardDeviationCalculator();
        simpleMovingAverage = new SMA(windowSize);
        dataWindowCloses = new LinkedList<>();

        this.windowSize = windowSize;
        this.standardDeviation = standardDeviation;

    }

    /**
     * Set the values for upper and lower Bands
     * @param candleClose The last candles close
     */
    public void setBollingerBands(double candleClose){

        simpleMovingAverage.setSMA(candleClose);
        addCloses(candleClose);

        if (dataWindowCloses.size() == windowSize) {
            setBands();
        }

        periodsProcessed++;

    }

    /**
     * Add the closes to the dataWindowCloses using a CLL to maintain window size
     * @param candleClose the last candles close
     */
    private void addCloses(double candleClose) {
        if (dataWindowCloses.size() == windowSize) {
            dataWindowCloses.remove(0);
        }
        dataWindowCloses.add(candleClose);
    }

    /**
     * Set the upper and lower bands according to the algorithm
     * Assumes Population variance
     */
    private void setBands(){
        double stdDev = SDC.calculateStandardDeviation(dataWindowCloses);

        upperBand = simpleMovingAverage.getSMA() + (stdDev * this.standardDeviation);
        lowerBand = simpleMovingAverage.getSMA() - (stdDev * this.standardDeviation);
    }

    public double getUpperBand(){
        return upperBand;
    }

    public double getLowerBand(){
        return lowerBand;
    }

    /**
     * @return If there have been enough candles for the BB to calculate given the window size
     */
    public boolean isReady(){
        return periodsProcessed >= windowSize;
    }

    /**
     * Calculate the standard Deviation of the candle closes for n window size
     */
    private static class StandardDeviationCalculator {
        public double calculateStandardDeviation(List<Double> data) {
            if (data == null || data.isEmpty()) {
                throw new IllegalArgumentException("Data array cannot be null or empty");
            }

            double sum = 0.0;
            for (double num : data) {
                sum += num;
            }
            double mean = sum / data.size();

            double sumOfSquaredDifferences = 0.0;
            for (double num : data) {
                sumOfSquaredDifferences += Math.pow(num - mean, 2);
            }

            double variance = sumOfSquaredDifferences / data.size();

            return Math.sqrt(variance);
        }
    }

}


