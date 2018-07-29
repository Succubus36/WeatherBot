package ru.alexsumin.weatherbot.domain;

public enum WeatherStatus {

    THUNDERSTORM("thunderstorm"),
    DRIZZLE("drizzle"),
    RAIN("rain"),
    SNOW("snow"),
    ATMOSPHERE("atmosphere"),
    CLEAR("clear"),
    CLOUDS("clouds");

    private final String weather;

    private WeatherStatus(String weather) {
        this.weather = weather;
    }

    public static WeatherStatus getStatus(String str) {
        if (str.equalsIgnoreCase(THUNDERSTORM.weather)) return THUNDERSTORM;
        if (str.equalsIgnoreCase(DRIZZLE.weather)) return DRIZZLE;
        if (str.equalsIgnoreCase(RAIN.weather)) return RAIN;
        if (str.equalsIgnoreCase(SNOW.weather)) return SNOW;
        if (str.equalsIgnoreCase(ATMOSPHERE.weather)) return ATMOSPHERE;
        if (str.equalsIgnoreCase(CLEAR.weather)) return CLEAR;
        if (str.equalsIgnoreCase(CLOUDS.weather)) return CLOUDS;
        return null;
    }


    public static String getTranslatedOnRuWeather(WeatherStatus status) {
        switch (status) {
            case THUNDERSTORM:
                return "\"шторм\"";
            case DRIZZLE:
                return "\"изморось\"";
            case RAIN:
                return "\"дождь\"";
            case SNOW:
                return "\"снег\"";
            case ATMOSPHERE:
                return "\"атмосферное явление\"";
            case CLEAR:
                return "\"ясно\"";
            case CLOUDS:
                return "\"облачно\"";
        }
        throw new IllegalArgumentException();
    }
}