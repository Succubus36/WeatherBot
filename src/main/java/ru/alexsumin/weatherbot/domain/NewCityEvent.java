package ru.alexsumin.weatherbot.domain;

public class NewCityEvent {

    private String city;

    public NewCityEvent(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
