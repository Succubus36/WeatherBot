package ru.alexsumin.weatherbot.domain;

import org.springframework.context.ApplicationEvent;

public class NewCityEvent {

    private String city;

    public NewCityEvent(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
