package ru.alexsumin.weatherbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;
import java.util.Date;

@Slf4j
public class StartCityCommand extends Command{

    private static final String HOURS_QUESTION = "Отлично! Теперь скажи за сколько часов тебе присылать сообщения об изменении погоды?";
    private static final String UNKNOWN_CITY = "Не удалось определить город, попробуй ещё раз";

    private final Message message;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final WeatherService weatherService;

    public StartCityCommand(Message message, UserService userService,
                            SubscriptionService subscriptionService, WeatherService weatherService) {
        this.message = message;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.weatherService = weatherService;
    }

    @Override
    public SendMessage call() {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);

        String cityName = message.getText();
        try {
            WeatherStatus weatherStatus = weatherService.getCurrentWeatherStatus(cityName);

            Subscription subscription = new Subscription(user, weatherStatus, cityName);
            subscription.setTimestamp(new Date().getTime());
            subscriptionService.save(subscription);

            user.setSubscription(subscription);
            user.setCurrentMenu(CurrentMenu.START_NOTIFICATIONS);

            userService.save(user);

            return new SendMessage(chatId, HOURS_QUESTION);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SendMessage(chatId, UNKNOWN_CITY);
        }
    }
}
