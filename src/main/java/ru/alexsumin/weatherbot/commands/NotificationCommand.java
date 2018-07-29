package ru.alexsumin.weatherbot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.domain.ReplyKeyboardBuilder;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import org.telegram.telegrambots.api.objects.Message;

public class NotificationCommand extends Command {

    private final Message message;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public NotificationCommand(Message message, UserService userService, SubscriptionService subscriptionService) {
        this.message = message;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
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
            default:
                return new SendMessage(chatId, "Не понял, попробуй ещё раз");
        }
    }
}
