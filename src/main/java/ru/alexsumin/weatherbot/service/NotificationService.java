package ru.alexsumin.weatherbot.service;

import ru.alexsumin.weatherbot.domain.NotificationMessage;

public interface NotificationService {

    void createNotification(Long chatId, NotificationMessage notification, long delay);
}
