package ru.alexsumin.weatherbot.service;

import net.aksingh.owmjapis.model.param.WeatherData;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.alexsumin.weatherbot.bot.Bot;
import ru.alexsumin.weatherbot.domain.CancelNotificationEvent;
import ru.alexsumin.weatherbot.domain.NotificationMessage;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationServiceImpl implements NotificationService {


    private Bot bot;
    private WeatherService weatherService;
    private SubscriptionService subscriptionService;

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
        scheduledTasks = new HashMap<>();
        execService = Executors.newScheduledThreadPool(5);
    }

    @EventListener
    private void cancelNotification(CancelNotificationEvent event) {
        System.out.println("отменить нотификацию");
        Long keyForCancel = event.getChatId();
        if (scheduledTasks.containsKey(keyForCancel)) {
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

    private void createNotification(Long chatId, NotificationMessage notification, long delay) {
        scheduledTasks.remove(chatId);
        Runnable notificationTask = () -> {
            SendMessage message = new SendMessage(chatId, notification.getMessage());
            bot.sendResponse(message);
        };
        ScheduledFuture<?> scheduledFuture = execService.schedule(notificationTask, delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(chatId, scheduledFuture);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void everyHourTaskTest() {
        System.out.println("every hour task!!!");

        List<Subscription> subscriptions = subscriptionService.getActiveSubscriptions();

        subscriptions.forEach(subscription -> {

            String city = subscription.getCity();
            long lastupdated = subscription.getTimestamp();
            long timeToAlert = subscription.getTimeToAlert() * 60 * 60 * 1000;
            WeatherStatus status = subscription.getWeatherStatus();
            List<WeatherData> forecast = weatherService.getForecastByCity(city);

            for (WeatherData wd : forecast) {

                if (wd.getDateTime().getTime() < lastupdated + timeToAlert)
                    continue;

                if (!status.equals(WeatherStatus.getStatus(wd.getWeatherList().get(0).getMainInfo()))) {

                    Long userForNotificate = subscription.getUser().getId();
                    WeatherStatus st = WeatherStatus.getStatus(wd.getWeatherList().get(0).getMainInfo());
                    Long time = wd.getDateTime().getTime() - lastupdated - timeToAlert;

                    createNotification(userForNotificate, new NotificationMessage(st, subscription.getTimeToAlert()), time);

                    subscription.setTimestamp(wd.getDateTime().getTime());
                    subscription.setWeatherStatus(st);

                    subscriptionService.save(subscription);
                }
            }
        });
    }

}
