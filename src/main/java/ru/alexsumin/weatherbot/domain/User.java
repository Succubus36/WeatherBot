package ru.alexsumin.weatherbot.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {

    @Id
    @NotNull
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    private Subscription subscription;

    @Enumerated(value = EnumType.STRING)
    private CurrentMenu currentMenu;

    public User() {
    }

    public User(@NotNull Long id) {
        this.id = id;
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

    public CurrentMenu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(CurrentMenu currentMenu) {
        this.currentMenu = currentMenu;
    }
}
