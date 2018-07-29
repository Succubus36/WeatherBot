package ru.alexsumin.weatherbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.alexsumin.weatherbot.commands.Command;
import ru.alexsumin.weatherbot.domain.CommandFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Bot extends TelegramLongPollingBot {

    private ExecutorService executorService;

    private final CommandFactory commandFactory;

    public Bot(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    @PostConstruct
    private void init() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Command command = commandFactory.getCommand(message);

            CompletableFuture.supplyAsync(() -> {
                try {
                    return command.call();
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

    public synchronized void sendResponse(SendMessage response) {
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
