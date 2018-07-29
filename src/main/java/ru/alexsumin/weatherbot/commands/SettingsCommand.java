package ru.alexsumin.weatherbot.commands;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.domain.ReplyKeyboardBuilder;
import ru.alexsumin.weatherbot.service.UserService;

public class SettingsCommand extends Command {

    private final Message message;
    private final UserService userService;

    public SettingsCommand(Message message, UserService userService) {
        this.message = message;
        this.userService = userService;
    }

    @Override
    public SendMessage call() {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);
        String text = message.getText();
        switch (text) {
            case "Изменить": {
                user.setCurrentMenu(CurrentMenu.CHANGE_CITY);
                userService.save(user);
                return ReplyKeyboardBuilder.create(chatId)
                        .setText("Пришли название города, чтобы я смог присылать тебе оповещения " +
                                "о изменении погоды в нем")
                        .row()
                        .button("Назад")
                        .endRow()
                        .build();
            }
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
            default:
                return new SendMessage(chatId, "Не понял, попробуй ещё раз");
        }
    }
}
