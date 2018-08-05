package ru.alexsumin.weatherbot.bot;

import lombok.extern.slf4j.Slf4j;
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
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
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
            log.info("Received message: " + message.getText() + " from user: " + message.getChatId());
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
                        log.error(throwable.getMessage());
                        return null;
                    });
        }

    }

    public synchronized void sendResponse(SendMessage response) {
        try {
            execute(response);
            log.info("Sent message: \"" + response.getText()
                        + "\" to user: " + response.getChatId());
        } catch (TelegramApiException e) {
            log.error("Couldn't send message: " + e.getMessage());
        }
    }

    @PreDestroy
    private void shutdown() {
        try {
            log.info("Attempt to shutdown executor");
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        } finally {
            executorService.shutdownNow();
            log.info("Shutdown executor finished");
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
