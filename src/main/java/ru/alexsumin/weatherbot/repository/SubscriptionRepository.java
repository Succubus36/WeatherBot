package ru.alexsumin.weatherbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.entity.Subscription;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    List<Subscription> findAllByIsActiveTrue();
}
