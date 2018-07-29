package ru.alexsumin.weatherbot.domain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.commands.*;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;

@Component
public class CommandFactory {


    private UserService userService;
    private SubscriptionService subscriptionService;
    private WeatherService weatherService;

    public CommandFactory(UserService userService, SubscriptionService subscriptionService,
                          WeatherService weatherService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.weatherService = weatherService;
    }

    public Command getCommand(Message message) {
        User user = userService.findById(message.getChatId());
        CurrentMenu menu = user.getCurrentMenu();

        switch (menu) {
            case START_CITY:
                return new StartCityCommand(message, userService, subscriptionService, weatherService);
            case START_NOTIFICATIONS:
                return new StartNotificationsCommand(message, userService, subscriptionService);
            case MENU:
                return new MenuCommand(message, userService, subscriptionService, weatherService);
            case SETTINGS:
                return new SettingsCommand(message, userService);
            case NOTIFICATIONS:
                return new NotificationCommand(message, userService, subscriptionService);
            case CHANGE_CITY:
                return new ChangeCityCommand(message, userService, subscriptionService, weatherService);
            default:
                return new StartCommand(message, userService);
        }
    }
}
