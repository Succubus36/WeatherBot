package ru.alexsumin.weatherbot.domain;

import ru.alexsumin.weatherbot.util.NumberUtil;

public class NotificationMessage {

    private WeatherStatus status;
    private int hours;

    public NotificationMessage(WeatherStatus status, int hours) {
        this.status = status;
        this.hours = hours;
    }

    public String getMessage() {
        return new StringBuilder("Через ").append(hours)
                .append(" ").append(NumberUtil.getFormattedHours(hours))
                .append(" ожидается изменение погоды на ")
                .append(WeatherStatus.getTranslatedOnRuWeather(status)).append(".").toString();
    }

}
