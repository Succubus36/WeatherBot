package ru.alexsumin.weatherbot.service;

import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.model.param.WeatherData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.alexsumin.weatherbot.bot.Bot;
import ru.alexsumin.weatherbot.domain.CancelNotificationEvent;
import ru.alexsumin.weatherbot.domain.NotificationMessage;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Bot bot;
    private final WeatherService weatherService;
    private final SubscriptionService subscriptionService;

    private Map<Long, ScheduledFuture> scheduledTasks;
    private List<Subscription> activeSubscriptions;
    private ScheduledExecutorService execService;

    public NotificationServiceImpl(Bot bot, WeatherService weatherService,
                                   SubscriptionService subscriptionService) {
        this.bot = bot;
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
    }

    @PostConstruct
    private void bootstrap() {
        updateSubscriptions();
        scheduledTasks = new ConcurrentHashMap<>();
        execService = Executors.newSingleThreadScheduledExecutor();
    }

    @TransactionalEventListener
    private void cancelNotification(CancelNotificationEvent event) {
        Long idForCancel = event.getChatId();
        log.info("Cancel notification event for id: " + idForCancel);
        if (scheduledTasks.containsKey(idForCancel)) {
            ScheduledFuture<?> forCancel = scheduledTasks.remove(event.getChatId());
            forCancel.cancel(true);
        }
    }

    private void updateSubscriptions() {
        activeSubscriptions = subscriptionService.getActiveSubscriptions();
        if (!activeSubscriptions.isEmpty()) {
            Set<String> cities = new HashSet<>();
            activeSubscriptions.forEach(subscription -> cities.add(subscription.getCity()));
        }
    }

    public void createNotification(Long chatId, NotificationMessage notification, long delay) {
        log.info("Create notification for user id: " + chatId);
        scheduledTasks.remove(chatId);
        Runnable notificationTask = () -> {
            SendMessage message = new SendMessage(chatId, notification.getMessage());
            bot.sendResponse(message);
        };
        ScheduledFuture<?> scheduledFuture = execService.schedule(notificationTask, delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(chatId, scheduledFuture);
    }

    @Scheduled(cron = "0 0  * * * *")
    public void everyHourForecastCheck() {
        log.info("Every hour task. Checking forecast");
        activeSubscriptions = subscriptionService.getActiveSubscriptions();
        activeSubscriptions.forEach(subscription -> {

            String city = subscription.getCity();
            long lastUpdated = subscription.getTimestamp();
            long timeToAlert = subscription.getTimeToAlert() * 60 * 60 * 1000;
            WeatherStatus status = subscription.getWeatherStatus();
            List<WeatherData> forecast = weatherService.getForecastByCity(city);

            for (WeatherData wd : forecast) {

                if (wd.getDateTime().getTime() < lastUpdated + timeToAlert)
                    continue;

                if (!status.equals(WeatherStatus.getStatus(wd.getWeatherList().get(0).getMainInfo()))) {

                    Long idUserForNotification = subscription.getUser().getId();
                    WeatherStatus st = WeatherStatus.getStatus(wd.getWeatherList().get(0).getMainInfo());
                    Long delay = wd.getDateTime().getTime() - lastUpdated - timeToAlert;

                    createNotification(idUserForNotification, new NotificationMessage(st, subscription.getTimeToAlert()), delay);

                    subscription.setTimestamp(wd.getDateTime().getTime());
                    subscription.setWeatherStatus(st);

                    subscriptionService.save(subscription);
                }
            }
        });
    }

    @PreDestroy
    private void shutdown() {
        try {
            log.info("Attempt to shutdown scheduled executor");
            execService.shutdown();
            execService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        } finally {
            execService.shutdownNow();
            log.info("Shutdown scheduled executor finished");
        }
    }

}
