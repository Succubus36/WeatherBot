package ru.alexsumin.weatherbot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.domain.CurrentMenu;
import ru.alexsumin.weatherbot.domain.ReplyKeyboardBuilder;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;

public class MenuCommand extends Command {

    private static final String INFO = "Я бот, который любезно отправляет оповещения об изменениях погоды в твоём городе. " +
            "Иногда это может быть весьма полезно. Например, со мной дождь уже не застанет тебя врасплох. " +
            "\nЕсли у тебя есть трудности с добавлением своего города, попробуй написать на английском, " +
            "с указанием страны, например: London, Uk." +
            "\nЯ только в начале своего жизненного пути и, если ты хочешь помочь мне в развитии, пиши моему " +
            "создателю @nimus все пожелания и предложения.";
    private static final String NOTIFICATIONS = "Хочешь, чтобы я сам присылал тебе сообщения об изменении погоды?" +
            "Напиши за сколько часов тебя предупредить(от 1 до 24)";


    private final Message message;
    private final UserService userService;
    private final WeatherService weatherService;

    public MenuCommand(Message message, UserService userService, WeatherService weatherService) {
        this.message = message;
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @Override
    public SendMessage call() {
        Long chatId = message.getChatId();
        User user = userService.findById(chatId);
        String text = message.getText();
        switch (text) {
            case "Информация":
                return new SendMessage(chatId, INFO);
            case "Погода сейчас": {
                try {
                    String city = user.getSubscription().getCity();
                    WeatherStatus status = weatherService.getCurrentWeatherStatus(city);
                    StringBuilder answer = new StringBuilder("Сейчас в городе ").append(city)
                            .append(" ").append(WeatherStatus.getTranslatedOnRuWeather(status));
                    return new SendMessage(chatId, answer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SendMessage(chatId, "Не удалось узнать погоду, попробуй попозже ещё разик");
                }
            }
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
            case "Настройки": {
                Subscription subscription = user.getSubscription();
                String city = subscription.getCity();
                user.setCurrentMenu(CurrentMenu.SETTINGS);
                userService.save(user);
                return ReplyKeyboardBuilder.create(chatId)
                        .setText("Ты подписан на обновления в городе: " + city + ".")
                        .row()
                        .button("Изменить")
                        .button("Назад")
                        .endRow()
                        .build();
            }
            default:
                return new SendMessage(chatId, "Не понял, попробуй ещё раз");
        }
    }
}
