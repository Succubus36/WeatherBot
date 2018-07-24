package ru.alexsumin.weatherbot.domain.entity;

import ru.alexsumin.weatherbot.domain.WeatherStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weather_state")
public class WeatherState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_state_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "city")
    private String city;

    @Enumerated(value = EnumType.STRING)
    private WeatherStatus weatherStatus;

    public WeatherState() {
    }

    public WeatherState(String city, WeatherStatus weatherStatus) {
        this.city = city;
        this.weatherStatus = weatherStatus;
        this.timestamp = new Date().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
