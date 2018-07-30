package ru.alexsumin.weatherbot.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.alexsumin.weatherbot.domain.WeatherStatus;

import javax.persistence.*;
@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
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
}
