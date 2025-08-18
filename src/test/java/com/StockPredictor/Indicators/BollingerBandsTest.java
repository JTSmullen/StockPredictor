package com.StockPredictor.Indicators;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BollingerBandsTest {

    @Test
    void constructorRejectsNonPositiveWindowSize() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new BollingerBands(0, 2.0));
        assertTrue(ex1.getMessage().contains("Window Size must be greater than zero"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new BollingerBands(-5, 2.0));
        assertTrue(ex2.getMessage().contains("Window Size must be greater than zero"));
    }

    @Test
    void constructorRejectsNonPositiveStdDevMultiplier() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new BollingerBands(5, 0.0));
        assertTrue(ex1.getMessage().contains("standard deviation must be greater than zero"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new BollingerBands(5, -1.0));
        assertTrue(ex2.getMessage().contains("standard deviation must be greater than zero"));
    }

    @Test
    void bandsRemainZeroUntilReady_andIsReadyFalse() {
        BollingerBands bb = new BollingerBands(3, 2.0);

        bb.setBollingerBands(10.0);
        bb.setBollingerBands(12.0);

        assertEquals(0.0, bb.getUpperBand(), 1e-9);
        assertEquals(0.0, bb.getLowerBand(), 1e-9);
        assertFalse(bb.isReady(), "Should not be ready until we have a full window");
    }

    @Test
    void isReadyTrueAtFirstFullWindow_andDeterministicValues() {
        BollingerBands bb = new BollingerBands(3, 2.0);

        bb.setBollingerBands(10.0);
        bb.setBollingerBands(12.0);
        bb.setBollingerBands(14.0);

        assertTrue(bb.isReady(), "Should be ready after exactly windowSize closes");

        double expectedSma = 12.0;
        double expectedStd = Math.sqrt(8.0 / 3.0);
        double multiplier = 2.0;
        double expectedUpper = expectedSma + expectedStd * multiplier;
        double expectedLower = expectedSma - expectedStd * multiplier;

        assertEquals(expectedUpper, bb.getUpperBand(), 1e-9);
        assertEquals(expectedLower, bb.getLowerBand(), 1e-9);
    }

    @Test
    void slidingWindowUpdatesBands() {
        BollingerBands bb = new BollingerBands(3, 2.0);

        bb.setBollingerBands(10.0);
        bb.setBollingerBands(12.0);
        bb.setBollingerBands(14.0);

        double upper1 = bb.getUpperBand();
        double lower1 = bb.getLowerBand();

        bb.setBollingerBands(16.0);

        assertTrue(bb.isReady());
        assertNotEquals(upper1, bb.getUpperBand());
        assertNotEquals(lower1, bb.getLowerBand());

        double expectedSma = 14.0;
        double expectedStd = Math.sqrt(8.0 / 3.0);
        double expectedUpper = expectedSma + expectedStd * 2.0;
        double expectedLower = expectedSma - expectedStd * 2.0;

        assertEquals(expectedUpper, bb.getUpperBand(), 1e-9);
        assertEquals(expectedLower, bb.getLowerBand(), 1e-9);
    }

    @Test
    void standardDeviationCalculator_throwsOnNullAndEmpty_viaReflection() throws Exception {
        Class<?> clazz = Class.forName("com.StockPredictor.Indicators.BollingerBands$StandardDeviationCalculator");

        Constructor<?> ctor = clazz.getDeclaredConstructor();
        ctor.setAccessible(true);
        Object sdc = ctor.newInstance();

        Method method = clazz.getDeclaredMethod("calculateStandardDeviation", java.util.List.class);
        method.setAccessible(true);

        InvocationTargetException ex1 = assertThrows(InvocationTargetException.class,
                () -> method.invoke(sdc, new Object[]{null}));
        assertTrue(ex1.getCause() instanceof IllegalArgumentException);
        assertTrue(ex1.getCause().getMessage().contains("Data array cannot be null or empty"));

        InvocationTargetException ex2 = assertThrows(InvocationTargetException.class,
                () -> method.invoke(sdc, Collections.emptyList()));
        assertTrue(ex2.getCause() instanceof IllegalArgumentException);
        assertTrue(ex2.getCause().getMessage().contains("Data array cannot be null or empty"));
    }
}
