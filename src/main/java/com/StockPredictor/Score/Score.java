package com.StockPredictor.Score;

import com.StockPredictor.Indicators.Indicators.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Score {
    private final double entryPrice;
    private final EMA ema;
    private final SMA sma;
    private final ATR atr;
    private final RSI rsi;
    private final BollingerBands bb;

    public Score(double entryPrice, EMA ema, SMA sma, RSI rsi, ATR atr, BollingerBands bb) {

        this.entryPrice = entryPrice;
        this.ema = ema;
        this.sma = sma;
        this.atr = atr;
        this.rsi = rsi;
        this.bb = bb;

    }

    public double calculateScore(double currentPrice) {
        double points = 0;
        // Stock Performance
        double pctChange = ((currentPrice - entryPrice) / entryPrice) * 100;
        points += pctChange;

        // EMA
        double emaVal = ema.getExponentialMovingAverage();
        double smaVal = sma.getSMA();
        if (currentPrice > emaVal && emaVal > smaVal) points += 5;
        else if (currentPrice > emaVal) points += 2;
        else if (currentPrice < emaVal && emaVal < smaVal) points -= 3;

        // RSI
        double rsiVal = rsi.getRelativeStrengthIndex();
        if (rsiVal < 30) points += 5;
        else if (rsiVal > 70) points -= 5;
        else points += 3;

        // Bollinger Bands
        if (currentPrice >= bb.getUpperBand()) points -= 4;
        else if (currentPrice <= bb.getLowerBand()) points += 6;
        else if (currentPrice > bb.getMiddleBand()) points += 4;

        // 5. ATR
        double atrPct = atr.getAverageTrueRange() / currentPrice * 100;
        points += Math.min(5, atrPct);

        // Round to 1 decimal place
        return BigDecimal.valueOf(points).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

}
