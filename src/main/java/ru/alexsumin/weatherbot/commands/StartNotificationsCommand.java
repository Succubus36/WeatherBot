package ru.alexsumin.weatherbot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.ReplyKeyboardBuilder;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.util.NumberUtil;

public class StartNotificationsCommand  extends Command {

    private static final String WRONG_HOURS = "Количество часов должно быть от 1 до 24! Попробуй еще раз";

    private final Message message;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public StartNotificationsCommand(Message message, UserService userService,
                                     SubscriptionService subscriptionService) {
        this.message = message;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public SendMessage call() {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);
        try {
            int hours = Integer.parseInt(message.getText());
            if (!NumberUtil.isInTheRangeFromOneToTwentyFour(hours)) {
                return new SendMessage(chatId, WRONG_HOURS);
            }

            Subscription subscription = user.getSubscription();
            subscription.setTimeToAlert(hours);
            subscription.setIsActive(true);
            subscriptionService.save(subscription);
            user.setCurrentMenu(CurrentMenu.MENU);
            userService.save(user);
            return ReplyKeyboardBuilder.create(chatId)
                    .setText("Отлично! Буду присылать уведомления за " +
                            hours + " " + NumberUtil.getFormattedHours(hours) + ".\nМеню:")
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
}
