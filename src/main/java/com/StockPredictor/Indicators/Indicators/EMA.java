package com.StockPredictor.Indicators.Indicators;


public class EMA {

    private final SMA simpleMovingAverage;

    private final double multiplier;

    private double exponentialMovingAverage = 0.0;

    public EMA(int windowSize) {

        simpleMovingAverage = new SMA(windowSize);
        multiplier = (double) 2 / (windowSize + 1);

    }

    public void setEMA(double candleClose) {

        if (exponentialMovingAverage == 0){
            simpleMovingAverage.setSMA(candleClose);
            if (simpleMovingAverage.isReady()){
                exponentialMovingAverage = simpleMovingAverage.getSMA();
            }
        } else {
            exponentialMovingAverage = (candleClose - exponentialMovingAverage) * multiplier + exponentialMovingAverage;
        }

    }

    public boolean isReady(){
        return simpleMovingAverage.isReady();
    }

    public double getExponentialMovingAverage(){
        return exponentialMovingAverage;
    }

}
