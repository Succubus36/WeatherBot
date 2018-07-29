package ru.alexsumin.weatherbot.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberUtilTest {

    @Test
    public void isInTheRangeFromOneToTwentyFour() {
        assertEquals(true, NumberUtil.isInTheRangeFromOneToTwentyFour(1));
        assertEquals(true, NumberUtil.isInTheRangeFromOneToTwentyFour(10));
        assertEquals(true, NumberUtil.isInTheRangeFromOneToTwentyFour(24));
        assertEquals(false, NumberUtil.isInTheRangeFromOneToTwentyFour(25));
        assertEquals(false, NumberUtil.isInTheRangeFromOneToTwentyFour(0));
        assertEquals(false, NumberUtil.isInTheRangeFromOneToTwentyFour(418));
    }

    @Test
    public void getFormattedHours() {
        assertEquals("часов", NumberUtil.getFormattedHours(5));
        assertEquals("час", NumberUtil.getFormattedHours(1));
        assertEquals("часа", NumberUtil.getFormattedHours(2));
        assertEquals("часов", NumberUtil.getFormattedHours(14));
        assertEquals("часа", NumberUtil.getFormattedHours(24));
        assertEquals("час", NumberUtil.getFormattedHours(21));


    }
}