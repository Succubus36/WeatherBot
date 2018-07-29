package ru.alexsumin.weatherbot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.service.UserService;

public class StartCommand extends Command{

    private static final String HELLO = "Привет! Красивое приветствие." +
            "А сейчас небольшое интервью.\n" +
            "Впиши свой город:";

    private final Message message;
    private final UserService userService;

    public StartCommand(Message message, UserService userService) {
        this.message = message;
        this.userService = userService;
    }

    @Override
    public SendMessage call() {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);

        user.setCurrentMenu(CurrentMenu.START_CITY);
        userService.save(user);
        return new SendMessage(chatId, HELLO);
    }
}
