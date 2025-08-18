package com.StockPredictor.Indicators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RSITest {

    @Test
    void testConstructorThrowsForInvalidWindow() {
        assertThrows(IllegalArgumentException.class, () -> new RSI(0));
        assertThrows(IllegalArgumentException.class, () -> new RSI(-5));
    }

    @Test
    void testIsReadyBeforeEnoughData() {
        RSI rsi = new RSI(14);
        for (int i = 0; i < 10; i++) {
            rsi.addCandle(100 + i);
        }
        assertFalse(rsi.isReady(), "RSI should not be ready before window is filled");
    }

    @Test
    void testRSIWithKnownData() {
        // Example dataset taken from RSI calculation guides
        double[] closes = {44.34,44.09,44.15,43.61,44.33,44.83,45.10,45.42,45.84,46.08,
                45.89,46.03,45.61,46.28,46.28,46.00,46.03,46.41,46.22,45.64,
                46.21,46.25,45.71,46.45,45.78,45.35,44.03,44.18,44.22,44.57,
                43.42,42.66,43.13};

        RSI rsi = new RSI(14);
        double lastRsi = 0.0;

        for (double close : closes) {
            rsi.addCandle(close);
            if (rsi.isReady()) {
                lastRsi = rsi.getRelativeStrengthIndex();
            }
        }

        // Known RSI for this dataset should be ~37.77 (per Wilder's example)
        assertTrue(lastRsi > 37 && lastRsi < 38, "RSI should be around 37.77, got " + lastRsi);
    }

    @Test
    void testAlwaysIncreasingPrices() {
        RSI rsi = new RSI(5);
        double[] closes = {10,11,12,13,14,15,16,17,18,19,20};

        for (double close : closes) {
            rsi.addCandle(close);
        }

        assertTrue(rsi.isReady());
        assertEquals(100.0, rsi.getRelativeStrengthIndex(), 0.001,
                "RSI should be ~100 for constant gains");
    }

    @Test
    void testAlwaysDecreasingPrices() {
        RSI rsi = new RSI(5);
        double[] closes = {20,19,18,17,16,15,14,13,12,11,10};

        for (double close : closes) {
            rsi.addCandle(close);
        }

        assertTrue(rsi.isReady());
        assertEquals(0.0, rsi.getRelativeStrengthIndex(), 0.001,
                "RSI should be ~0 for constant losses");
    }
}
