package ru.alexsumin.weatherbot.domain.entity;

import ru.alexsumin.weatherbot.domain.WeatherStatus;

import javax.persistence.*;

@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "time_to_alert")
    private Integer timeToAlert;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "city")
    private String city;

    @Enumerated(value = EnumType.STRING)
    private WeatherStatus weatherStatus;


    public Subscription() {
    }

    public Subscription(User user, WeatherStatus weatherStatus, String city) {
        this.user = user;
        this.weatherStatus = weatherStatus;
        this.city = city;
        this.isActive = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTimeToAlert() {
        return timeToAlert;
    }

    public void setTimeToAlert(Integer timeToAlert) {
        this.timeToAlert = timeToAlert;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }
}
