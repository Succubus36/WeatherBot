package ru.alexsumin.weatherbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.alexsumin.weatherbot.handler.MessageHandler;
import ru.alexsumin.weatherbot.handler.MessageHandlerImpl;
import ru.alexsumin.weatherbot.service.CityService;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Bot extends TelegramLongPollingBot {

    private ExecutorService executorService;

    private UserService userService;
    private WeatherService weatherService;
    private SubscriptionService subscriptionService;
    private CityService cityService;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    public Bot(UserService userService, WeatherService weatherService,
               SubscriptionService subscriptionService, CityService cityService) {
        this.userService = userService;
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
        this.cityService = cityService;
    }

    @PostConstruct
    private void init() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {


            MessageHandler handler
                    = new MessageHandlerImpl(update.getMessage(), userService,
                    weatherService, subscriptionService, cityService);

            CompletableFuture.supplyAsync(() -> {
                try {
                    return handler.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executorService)
                    .thenAcceptAsync(this::sendResponse, executorService)
                    .exceptionally(throwable -> {
                        System.out.println(throwable.getMessage());
                        return null;
                    });

        }

    }

    private synchronized void sendResponse(SendMessage response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
