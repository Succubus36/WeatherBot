package ru.alexsumin.weatherbot.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.alexsumin.weatherbot.domain.CancelNotificationEvent;
import ru.alexsumin.weatherbot.domain.NewCityEvent;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.repository.SubscriptionRepository;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ApplicationEventPublisher publisher;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(ApplicationEventPublisher publisher,
                                   SubscriptionRepository subscriptionRepository) {
        this.publisher = publisher;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {

        if (subscription.getActive() & (subscription.getCity() != null))
            publisher.publishEvent(new NewCityEvent(this, subscription.getCity()));

        if (subscription.getId() != null) {
            Subscription old = subscriptionRepository.findById(subscription.getId()).get();
            if ((old.getActive()) & (!subscription.getActive()))
                publisher.publishEvent(new CancelNotificationEvent(this, subscription.getUser().getId()));
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findAllByIsActiveTrue();
    }
}
