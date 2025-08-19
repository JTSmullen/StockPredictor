package com.StockPredictor.Score;

import com.StockPredictor.Indicators.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Score class.
 * Covers all indicator branches and stock performance cases.
 */
public class ScoreTest {

    // --- Stub indicator classes for testing ---
    static class TestEMA extends EMA {
        private final double value;
        public TestEMA(double value) { super(1); this.value = value; }
        @Override public double getExponentialMovingAverage() { return value; }
    }

    static class TestSMA extends SMA {
        private final double value;
        public TestSMA(double value) { super(1); this.value = value; }
        @Override public double getSMA() { return value; }
    }

    static class TestRSI extends RSI {
        private final double value;
        public TestRSI(double value) { super(1); this.value = value; }
        @Override public double getRelativeStrengthIndex() { return value; }
    }

    static class TestATR extends ATR {
        private final double value;
        public TestATR(double value) { super(1); this.value = value; }
        @Override public double getAverageTrueRange() { return value; }
    }

    static class TestBB extends BollingerBands {
        private final double lower, middle, upper;
        public TestBB(double lower, double middle, double upper) {
            super(1,1); this.lower = lower; this.middle = middle; this.upper = upper;
        }
        @Override public double getLowerBand() { return lower; }
        @Override public double getMiddleBand() { return middle; }
        @Override public double getUpperBand() { return upper; }
    }

    // --- Tests ---

    @Test
    void testStockPerformanceGain() {
        Score score = new Score(
                100,
                new TestEMA(90),
                new TestSMA(80),
                new TestRSI(50), // neutral
                new TestATR(1),
                new TestBB(70, 85, 120) // within bands
        );
        double result = score.calculateScore(110);
        // +10% +5 (EMA>SMA) +3 (RSI neutral) +4 (above middle) +0.9 ATR = 22.9
        assertEquals(22.9, result, 0.1);
    }

    @Test
    void testStockPerformanceLoss() {
        Score score = new Score(
                100,
                new TestEMA(120),
                new TestSMA(130),
                new TestRSI(80), // overbought
                new TestATR(5),
                new TestBB(90, 95, 120) // current=90 at lower band
        );
        double result = score.calculateScore(90);
        // -10% -3 (below EMA/SMA) -5 (RSI>70) +6 (touch lower) +5 ATR = -7
        assertEquals(-7.0, result, 0.1);
    }

    @Test
    void testRSIOverSold() {
        Score score = new Score(
                100,
                new TestEMA(90),
                new TestSMA(80),
                new TestRSI(20), // oversold
                new TestATR(1),
                new TestBB(80, 85, 90) // current=95 > upper → -4
        );
        double result = score.calculateScore(95);
        // -5% +5 (EMA>SMA) +5 (RSI<30) -4 (above upper) +1.1 ATR = 2.1
        assertEquals(2.1, result, 0.1);
    }

    @Test
    void testRSIOverBought() {
        Score score = new Score(
                100,
                new TestEMA(90),
                new TestSMA(80),
                new TestRSI(75), // overbought
                new TestATR(2),
                new TestBB(70, 85, 95) // current=92 > middle
        );
        double result = score.calculateScore(92);
        // -8% +5 (EMA>SMA) -5 (RSI>70) +4 (above middle) +2.2 ATR = -1.8
        assertEquals(-1.8, result, 0.1);
    }

    @Test
    void testPriceTouchesUpperBand() {
        Score score = new Score(
                100,
                new TestEMA(90),
                new TestSMA(80),
                new TestRSI(50),
                new TestATR(1),
                new TestBB(70, 85, 100) // current=100 == upper
        );
        double result = score.calculateScore(100);
        // 0% +5 (EMA>SMA) +3 (neutral RSI) -4 (touch upper) +1.0 ATR = 5.0
        assertEquals(5.0, result, 0.1);
    }

    @Test
    void testPriceTouchesLowerBand() {
        Score score = new Score(
                100,
                new TestEMA(90),
                new TestSMA(80),
                new TestRSI(50),
                new TestATR(1),
                new TestBB(90, 95, 105) // current=90 == lower
        );
        double result = score.calculateScore(90);
        // -10% +5 (EMA>SMA) +3 (neutral RSI) +6 (touch lower) +1.1 ATR = 5.1
        assertEquals(0.1, result, 0.1);
    }

    @Test
    void testATRBonusCappedAt5() {
        Score score = new Score(
                100,
                new TestEMA(50),
                new TestSMA(40),
                new TestRSI(50),
                new TestATR(20), // huge ATR → should cap at 5
                new TestBB(10, 20, 30)
        );
        double result = score.calculateScore(120);
        // +20% +5 (EMA>SMA) +3 (RSI neutral) +4 (above middle) +5 capped = 37
        assertEquals(29.0, result, 0.1);
    }
}
