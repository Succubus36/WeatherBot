package ru.alexsumin.weatherbot.domain;

import org.springframework.context.ApplicationEvent;

public class NewCityEvent extends ApplicationEvent {

    private String city;

    public NewCityEvent(Object source, String city) {
        super(source);
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
