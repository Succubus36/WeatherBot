package ru.alexsumin.weatherbot.domain;

import javax.persistence.*;

@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @OneToOne(mappedBy = "subscription", cascade = CascadeType.ALL)
    private User user;

    private Integer timeOfNotification;

    private Boolean isActive;

    @ManyToOne
    private City city;

    @Enumerated(value = EnumType.STRING)
    private CurrentStatus currentStatus;

    public Subscription() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTimeOfNotification() {
        return timeOfNotification;
    }

    public void setTimeOfNotification(Integer timeOfNotification) {
        this.timeOfNotification = timeOfNotification;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

}
