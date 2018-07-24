package ru.alexsumin.weatherbot.domain.entity;

import ru.alexsumin.weatherbot.domain.CurrentMenu;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {

    @Id
    @NotNull
    @Column(name = "user_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "current_menu")
    @Enumerated(value = EnumType.STRING)
    private CurrentMenu currentMenu;

    public User() {
    }

    public User(@NotNull Long id) {
        this.id = id;
        this.currentMenu = CurrentMenu.START;
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
