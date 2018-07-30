package ru.alexsumin.weatherbot.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.repository.SubscriptionRepository;
import ru.alexsumin.weatherbot.repository.UserRepository;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.WeatherService;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final WeatherService weatherService;
    private final SubscriptionService subscriptionService;

    public Bootstrap(WeatherService weatherService, SubscriptionService subscriptionService) {
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Initialization application...");
        initData();
    }

    private void initData() {
        List<Subscription> activeSubscriptions = subscriptionService.getActiveSubscriptions();

        log.info("Trying to update...");
        activeSubscriptions.forEach(subscription -> {
            try {
                WeatherStatus status = weatherService.getCurrentWeatherStatus(subscription.getCity());
                subscription.setWeatherStatus(status);
                subscription.setTimestamp(new Date().getTime());
                subscriptionService.save(subscription);
            } catch (Exception e) {
                log.error("Couldn't update weather: " + e);
            }
        });
    }
}
