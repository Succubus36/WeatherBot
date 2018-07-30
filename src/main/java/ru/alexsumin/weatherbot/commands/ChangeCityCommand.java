package ru.alexsumin.weatherbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.domain.ReplyKeyboardBuilder;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;

import java.util.Date;
@Slf4j
public class ChangeCityCommand extends Command {

    private static final String UNKNOWN_CITY = "Не удалось определить город, попробуй ещё раз";

    private final Message message;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final WeatherService weatherService;

    public ChangeCityCommand(Message message, UserService userService,
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
        String text = message.getText();

        switch (text) {
            case "Назад": {
                user.setCurrentMenu(CurrentMenu.MENU);
                userService.save(user);
                return ReplyKeyboardBuilder.create(chatId)
                        .setText("Меню:")
                        .row()
                        .button("Информация")
                        .button("Погода сейчас")
                        .endRow()
                        .row()
                        .button("Настройки")
                        .button("Уведомления")
                        .endRow()
                        .build();
            }
            default: {
                try {
                    WeatherStatus weatherStatus = weatherService.getCurrentWeatherStatus(text);

                    Subscription subscription = user.getSubscription();
                    subscription.setCity(text);
                    subscription.setTimestamp(new Date().getTime());
                    subscription.setWeatherStatus(weatherStatus);
                    subscriptionService.save(subscription);

                    user.setSubscription(subscription);
                    user.setCurrentMenu(CurrentMenu.MENU);
                    userService.save(user);
                    return ReplyKeyboardBuilder.create(chatId)
                            .setText("Отлично! Теперь ты подписан на изменения погоды в городе " + text +
                                    ". \nМеню:")
                            .row()
                            .button("Информация")
                            .button("Погода сейчас")
                            .endRow()
                            .row()
                            .button("Настройки")
                            .button("Уведомления")
                            .endRow()
                            .build();
                }
                catch (Exception e){
                    log.error(e.getMessage());
                    return new SendMessage(chatId, UNKNOWN_CITY);
                }
            }
        }
    }
}
