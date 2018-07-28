package ru.alexsumin.weatherbot.domain;

import org.springframework.context.ApplicationEvent;

public class CancelNotificationEvent extends ApplicationEvent {

    private Long chatId;

    public CancelNotificationEvent(Object source, Long chatId) {
        super(source);
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
