package ru.alexsumin.weatherbot.util;

public class NumberUtil {

    private NumberUtil() {
    }

    public static boolean isInTheRangeFromOneToTwentyFour(int hours) {
        return (((hours > 0) & (hours <= 24)));
    }

    public static String getFormattedHours(int hours) {
        switch (hours) {
            case 1:
            case 21:
                return "час";
            case 2:
            case 3:
            case 4:
            case 22:
            case 23:
            case 24:
                return "часа";
            default:
                return "часов";
        }
    }
}
