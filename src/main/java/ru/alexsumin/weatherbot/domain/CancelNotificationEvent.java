package ru.alexsumin.weatherbot.domain;

public class CancelNotificationEvent {

    private Long chatId;

    public CancelNotificationEvent(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
