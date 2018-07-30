package ru.alexsumin.weatherbot.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.alexsumin.weatherbot.domain.CurrentMenu;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Data
@ToString(exclude = "subscription")
@EqualsAndHashCode(exclude = "subscription")
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "current_menu")
    @Enumerated(value = EnumType.STRING)
    private CurrentMenu currentMenu;

    public User() {
    }

    public User(Long id) {
        this.id = id;
        this.currentMenu = CurrentMenu.START;
    }
}
