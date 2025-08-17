package com.StockPredictor.Indicators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class SMATest {

    private SMA sma;

    @BeforeEach
    void setUp() {
        sma = new SMA(3); // window size 3
    }

    @Test
    void testConstructorValid() {
        SMA validSMA = new SMA(5);
        assertNotNull(validSMA);
        assertEquals(0.0, validSMA.getSMA());
    }

    @Test
    void testConstructorInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SMA(0);
        });
        assertEquals("Window size must be positive", exception.getMessage());
    }

    @Test
    void testSMAWithFewerThanWindow() {
        sma.setSMA(10.0);
        assertEquals(0.0, sma.getSMA(), "SMA should be 0 until window is full");

        sma.setSMA(20.0);
        assertEquals(0.0, sma.getSMA(), "SMA should still be 0 until window is full");
    }

    @Test
    void testSMAWhenWindowFull() {
        sma.setSMA(10.0);
        sma.setSMA(20.0);
        sma.setSMA(30.0); // window is now full
        assertEquals(20.0, sma.getSMA(), 0.0001);

        sma.setSMA(40.0); // rolling update
        assertEquals((20.0 + 30.0 + 40.0)/3, sma.getSMA(), 0.0001);

        sma.setSMA(50.0);
        assertEquals((30.0 + 40.0 + 50.0)/3, sma.getSMA(), 0.0001);
    }

    @Test
    void testRollingSumCorrectness() {
        sma.setSMA(5.0);
        sma.setSMA(15.0);
        sma.setSMA(25.0);
        assertEquals(15.0, sma.getSMA(), 0.0001);

        // Add more and ensure old values are removed
        sma.setSMA(35.0); // removes 5.0
        assertEquals((15.0+25.0+35.0)/3, sma.getSMA(), 0.0001);

        sma.setSMA(45.0); // removes 15.0
        assertEquals((25.0+35.0+45.0)/3, sma.getSMA(), 0.0001);
    }
}
