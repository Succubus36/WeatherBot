package ru.alexsumin.weatherbot.handler;

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

public class MessageHandlerImpl extends MessageHandler {

    private static final String HOURS_QUESTION = "Отлично! Теперь скажи за сколько часов тебе присылать сообщения об изменении погоды?";
    private static final String HELLO = "Привет! Красивое приветствие." +
            "А сейчас небольшое интервью.\n" +
            "Впиши свой город:";
    private static final String UNKNOWN_CITY = "Не удалось определить город, попробуй ещё раз";
    private static final String WRONG_HOURS = "Количество часов должно быть от 1 до 24! Попробуй еще раз";
    private static final String INFO = "Тут небольшая справка";
    private static final String NOTIFICATIONS = "Хочешь, чтобы я сам присылал тебе сообщения об изменении погоды?" +
            "Напиши за сколько часов тебя предупредить(от 1 до 24)";
    private final Message message;
    private UserService userService;
    private WeatherService weatherService;
    private SubscriptionService subscriptionService;

    public MessageHandlerImpl(Message message, UserService userService,
                              WeatherService weatherService, SubscriptionService subscriptionService) {
        this.message = message;
        this.userService = userService;
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public SendMessage call() throws Exception {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);
        switch (user.getCurrentMenu()) {
            case START: {
                user.setCurrentMenu(CurrentMenu.START_CITY);
                userService.save(user);
                return new SendMessage(chatId, HELLO);
            }
            case START_CITY: {
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
                    e.printStackTrace();
                    return new SendMessage(chatId, UNKNOWN_CITY);
                }
            }
            case START_NOTIFICATIONS: {
                try {
                    int hours = Integer.parseInt(message.getText());
                    if (!((hours > 0) & (hours <= 24)))
                        return new SendMessage(chatId, WRONG_HOURS);

                    Subscription subscription = user.getSubscription();
                    subscription.setTimeToAlert(hours);
                    subscription.setActive(true);
                    subscriptionService.save(subscription);
                    user.setCurrentMenu(CurrentMenu.MENU);
                    userService.save(user);
                    return ReplyKeyboardBuilder.create(chatId)
                            .setText("Отлично!\nМеню:")
                            .row()
                            .button("Информация")
                            .button("Погода сейчас")
                            .endRow()
                            .row()
                            .button("Настройки")
                            .button("Уведомления")
                            .endRow()
                            .build();

                } catch (NumberFormatException e) {
                    return new SendMessage(chatId, WRONG_HOURS);
                }
            }
            case MENU: {
                String text = message.getText();
                switch (text) {
                    case "Информация":
                        return new SendMessage(chatId, INFO);
                    case "Погода сейчас":

                    case "Уведомления": {
                        user.setCurrentMenu(CurrentMenu.NOTIFICATIONS);
                        userService.save(user);
                        return ReplyKeyboardBuilder.create(chatId)
                                .setText(NOTIFICATIONS)
                                .row()
                                .button("Назад")
                                .button("Не хочу")
                                .endRow()
                                .build();
                    }
                    case "Настройки":
                        default:
                            return new SendMessage(chatId, "Не понял, попробуй ещё раз");
                }
                //return new SendMessage(chatId, "Тут будет меню");
            }
            case NOTIFICATIONS: {
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
                    case "Не хочу": {
                        Subscription subscription = user.getSubscription();
                        subscription.setActive(false);
                        user.setCurrentMenu(CurrentMenu.MENU);
                        userService.save(user);
                        subscriptionService.save(subscription);
                        return ReplyKeyboardBuilder.create(chatId)
                                .setText("Ну и ладно :(\nМеню:")
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
                }
            }
            default:
                return new SendMessage(chatId, "Не понял, попробуй ещё раз");
        }
    }
}
