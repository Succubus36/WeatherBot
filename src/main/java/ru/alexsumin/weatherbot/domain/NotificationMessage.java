package ru.alexsumin.weatherbot.domain;

public class NotificationMessage {

    private WeatherStatus status;
    private int hours;


    public NotificationMessage(WeatherStatus status, int hours) {
        this.status = status;
        this.hours = hours;
    }

    public String getMessage() {
        return new StringBuilder("Через ").append(hours)
                .append(" ").append(getFormattedHours()).append(" ожидается изменение погоды на ")
                .append(getTranslatedWeather()).append(".").toString();
    }

    private String getFormattedHours() {
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

    private String getTranslatedWeather() {
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
