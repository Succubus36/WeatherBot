package ru.alexsumin.weatherbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.entity.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
}
