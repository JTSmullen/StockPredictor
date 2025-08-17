package com.StockPredictor.Indicators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ATRTest {

    @Test
    void testInitialAtrComputation() {
        ATR atr = new ATR(3);

        // Provide 4 candles so ATR has enough data
        atr.setATR(10, 5, 8); // TR = max(5,2,3) = 5
        atr.setATR(12, 6, 7); // TR = max(6,5,1) = 6
        atr.setATR(11, 7, 10); // TR = max(4,1,3) = 4

        // First ATR is simple average of [5,6,4] = 5
        assertEquals(5.0, atr.getAverageTrueRange(), 1e-6);
    }

    @Test
    void testRecursiveAtrCalculation() {
        ATR atr = new ATR(3);

        // Warmup
        atr.setATR(10, 5, 8); // TR=5
        atr.setATR(12, 6, 7); // TR=6
        atr.setATR(11, 7, 10); // TR=4

        assertEquals(5.0, atr.getAverageTrueRange(), 1e-6);

        // Next candle
        atr.setATR(15, 10, 9); // TR = max(5,6,1) = 6
        // New ATR = ((5 * (3-1)) + 6) / 3 = (10+6)/3 = 5.333...
        assertEquals(5.333333, atr.getAverageTrueRange(), 1e-6);
    }

    @Test
    void testThrowsOnInvalidWindowSize() {
        assertThrows(IllegalArgumentException.class, () -> new ATR(0));
        assertThrows(IllegalArgumentException.class, () -> new ATR(-5));
    }

    @Test
    void testAtrDoesNotCalculateUntilWindowFilled() {
        ATR atr = new ATR(3);

        atr.setATR(10, 5, 8); // TR=5
        atr.setATR(12, 6, 10); // TR=6

        // Not enough data yet
        assertEquals(0.0, atr.getAverageTrueRange(), 1e-6);

        atr.setATR(11, 7, 11); // TR=4
        assertEquals(5.0, atr.getAverageTrueRange(), 1e-6);
    }

    @Test
    void testAtrWhenNotEnoughDataStillZero() {
        ATR atr = new ATR(3);

        // Add fewer than windowSize candles
        atr.setATR(10, 8, 9);  // TR computed but window not full

        // ATR should still be 0 since not enough data points
        assertEquals(0.0, atr.getAverageTrueRange(), 1e-6);
    }
}
