package com.StockPredictor.Indicators;

public class RSI {

    private final int windowSize;
    private Double lastClose = null;

    private double averageGain = 0.0;
    private double averageLoss = 0.0;
    private double relativeStrengthIndex = 0.0;

    private int periodsProcessed = 0;
    private double sumGains = 0.0;
    private double sumLosses = 0.0;

    public RSI(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be positive");
        }
        this.windowSize = windowSize;
    }

    public void addCandle(double candleClose) {
        if (lastClose == null) {
            lastClose = candleClose;
            return;
        }

        double change = candleClose - lastClose;
        lastClose = candleClose;

        double gain = Math.max(change, 0);
        double loss = Math.max(-change, 0);

        if (periodsProcessed < windowSize) {
            sumGains += gain;
            sumLosses += loss;
            periodsProcessed++;

            if (periodsProcessed == windowSize) {
                averageGain = sumGains / windowSize;
                averageLoss = sumLosses / windowSize;
                calculateRSI();
            }
        } else {
            averageGain = (averageGain * (windowSize - 1) + gain) / windowSize;
            averageLoss = (averageLoss * (windowSize - 1) + loss) / windowSize;
            calculateRSI();
        }
    }

    private void calculateRSI() {
        if (averageLoss == 0.0) {
            relativeStrengthIndex = 100.0;
        } else {
            double rs = averageGain / averageLoss;
            relativeStrengthIndex = 100 - (100 / (1 + rs));
        }
    }

    public boolean isReady() {
        return periodsProcessed >= windowSize;
    }

    public double getRelativeStrengthIndex() {
        return relativeStrengthIndex;
    }
}
