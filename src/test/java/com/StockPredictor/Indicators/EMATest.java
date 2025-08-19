package com.StockPredictor.Indicators;

import com.StockPredictor.Indicators.Indicators.EMA;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EMATest {

    @Test
    void testEMAIsNotReadyInitially() {
        EMA ema = new EMA(3);

        // Fewer than 3 closes → SMA not ready → EMA stays 0
        ema.setEMA(10);
        ema.setEMA(20);

        assertFalse(ema.isReady()); // SMA not yet filled
        assertEquals(0.0, ema.getExponentialMovingAverage(), 1e-9);
    }

    @Test
    void testEMAInitializesWithSMA() {
        EMA ema = new EMA(3);

        // After 3 closes, SMA ready → EMA initializes with SMA
        ema.setEMA(10);
        ema.setEMA(20);
        ema.setEMA(30);

        assertTrue(ema.isReady());
        assertEquals((10 + 20 + 30) / 3.0, ema.getExponentialMovingAverage(), 1e-9);
    }

    @Test
    void testEMAUpdatesAfterInitialization() {
        EMA ema = new EMA(3);

        ema.setEMA(10);
        ema.setEMA(20);
        ema.setEMA(30); // EMA initialized
        double initialEMA = ema.getExponentialMovingAverage();

        ema.setEMA(40); // update via recursive formula
        double alpha = 2.0 / (3 + 1);
        double expected = (40 - initialEMA) * alpha + initialEMA;

        assertEquals(expected, ema.getExponentialMovingAverage(), 1e-9);
    }

    @Test
    void testEMAHandlesSingleCandleThenInitialization() {
        EMA ema = new EMA(2);

        // First close → still not ready
        ema.setEMA(50);
        assertEquals(0.0, ema.getExponentialMovingAverage(), 1e-9);

        // Second close → SMA ready → EMA initializes with SMA
        ema.setEMA(60);
        assertEquals((50 + 60) / 2.0, ema.getExponentialMovingAverage(), 1e-9);
    }

    @Test
    void testEMAWithMultipleUpdates() {
        EMA ema = new EMA(2);

        ema.setEMA(10);
        ema.setEMA(20); // SMA ready → EMA = 15
        double prev = ema.getExponentialMovingAverage();

        // keep applying formula for next values
        double alpha = 2.0 / (2 + 1);

        ema.setEMA(30);
        double expected1 = (30 - prev) * alpha + prev;
        assertEquals(expected1, ema.getExponentialMovingAverage(), 1e-9);

        prev = ema.getExponentialMovingAverage();
        ema.setEMA(40);
        double expected2 = (40 - prev) * alpha + prev;
        assertEquals(expected2, ema.getExponentialMovingAverage(), 1e-9);
    }
}
