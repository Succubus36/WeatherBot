package ru.alexsumin.weatherbot.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
}
