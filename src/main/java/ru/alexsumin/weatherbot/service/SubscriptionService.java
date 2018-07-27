package ru.alexsumin.weatherbot.service;

import ru.alexsumin.weatherbot.domain.entity.Subscription;

import java.util.List;

public interface SubscriptionService {

    Subscription save(Subscription subscription);

    List<Subscription> getActiveSubscriptions();

}
