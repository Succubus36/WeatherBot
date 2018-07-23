package ru.alexsumin.weatherbot.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.City;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.domain.entity.WeatherState;
import ru.alexsumin.weatherbot.service.CityService;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;

public class MessageHandlerImpl extends MessageHandler {

    private static final String HOURS_QUESTION = "Отлично! Теперь скажи за сколько часов тебе присылать сообщения об изменении погоды?";
    private static final String HELLO = "Привет! Красивое приветствие." +
            "А сейчас небольшое интервью.\n" +
            "Впиши свой город:";
    private static final String UNKNOWN_CITY = "Не могу найти город, попробуй ещё раз";
    private static final String WRONG_HOURS = "Количество часов должно быть от 1 до 24!";
    private final Message message;
    private UserService userService;
    private WeatherService weatherService;
    private SubscriptionService subscriptionService;
    private CityService cityService;

    public MessageHandlerImpl(Message message, UserService userService,
                              WeatherService weatherService, SubscriptionService subscriptionService,
                              CityService cityService) {
        this.message = message;
        this.userService = userService;
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
        this.cityService = cityService;
    }

    @Override
    public SendMessage call() throws Exception {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);
        switch (user.getCurrentMenu()) {
            case START: {
                user.setCurrentMenu(CurrentMenu.START_CITY);
                userService.save(user);
                System.out.println("Начало, приветствие, вопрос по городу");
                return new SendMessage(chatId, HELLO);
            }
            case START_CITY: {
                System.out.println("Разберем ответ по городу");
                String cityName = message.getText();
                try {
                    WeatherStatus weatherStatus = weatherService.getCurrentWeatherStatus(cityName);
                    City city;
                    if (cityService.checkForExist(cityName)) {
                        city = cityService.findByName(cityName);
                        System.out.println("нашёл город");
                    } else {
                        city = new City(cityName);
                        cityService.save(city);
                        System.out.println("сохранил новый город");
                    }
                    WeatherState state = new WeatherState(weatherStatus);
                    Subscription subscription = new Subscription(user, city);
                    subscription.setWeatherState(state);
                    subscriptionService.save(subscription);

                    user.setCurrentMenu(CurrentMenu.START_NOTIFICATIONS);
                    userService.save(user);

                    return new SendMessage(chatId, HOURS_QUESTION);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("не нашёл город");
                    return new SendMessage(chatId, UNKNOWN_CITY);
                }
            }
            case START_NOTIFICATIONS: {
                try {
                    int hours = Integer.parseInt(message.getText());
                    if ((0 < hours) & (hours <= 24))
                        return new SendMessage(chatId, WRONG_HOURS);
                    Subscription subscription = user.getSubscription();
                    subscription.setTimeOfNotification(hours);
                    subscriptionService.save(subscription);
                    user.setCurrentMenu(CurrentMenu.MENU);
                } catch (NumberFormatException e) {
                    return new SendMessage(chatId, WRONG_HOURS);
                }
            }
            case MENU:
                return new SendMessage(chatId, "Тут будет меню");
            default:
                return new SendMessage(chatId, "Не понял, попробуй ещё раз");
        }
    }
}
