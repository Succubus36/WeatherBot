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





//    private final Message message;
//    private UserService userService;
//    private WeatherService weatherService;
//    private SubscriptionService subscriptionService;
//
//    public MessageHandlerImpl(Message message, UserService userService,
//                              WeatherService weatherService, SubscriptionService subscriptionService) {
//        this.message = message;
//        this.userService = userService;
//        this.weatherService = weatherService;
//        this.subscriptionService = subscriptionService;
//    }

    @Override
    public SendMessage call() throws Exception {
//        Long chatId = message.getChatId();
//        User user = userService.findById(chatId);
//        switch (user.getCurrentMenu()) {
//            case START: {
//                user.setCurrentMenu(CurrentMenu.START_CITY);
//                userService.save(user);
//                return new SendMessage(chatId, HELLO);
//            }
//            case START_CITY: {
//                String cityName = message.getText();
//                try {
//                    WeatherStatus weatherStatus = weatherService.getCurrentWeatherStatus(cityName);
//
//                    Subscription subscription = new Subscription(user, weatherStatus, cityName);
//                    subscription.setTimestamp(new Date().getTime());
//                    subscriptionService.save(subscription);
//
//                    user.setSubscription(subscription);
//                    user.setCurrentMenu(CurrentMenu.START_NOTIFICATIONS);
//
//                    userService.save(user);
//
//                    return new SendMessage(chatId, HOURS_QUESTION);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return new SendMessage(chatId, UNKNOWN_CITY);
//                }
//            }
//            case START_NOTIFICATIONS: {
//
//            }
//            case MENU: {
//
//            }
//            case NOTIFICATIONS: {
//
//            }
//            case SETTINGS:{
//
//        }
        return null;
    }
}
