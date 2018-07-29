package ru.alexsumin.weatherbot.domain;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardBuilder {

    private Long chatId;
    private String text;

    private List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow row;

    private ReplyKeyboardBuilder() {}

    public static ReplyKeyboardBuilder create() {
        ReplyKeyboardBuilder builder = new ReplyKeyboardBuilder();
        return builder;
    }

    public static ReplyKeyboardBuilder create(Long chatId) {
        ReplyKeyboardBuilder builder = new ReplyKeyboardBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    public ReplyKeyboardBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public ReplyKeyboardBuilder setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public ReplyKeyboardBuilder row() {
        row = new KeyboardRow();
        return this;
    }

    public ReplyKeyboardBuilder endRow() {
        this.keyboard.add(this.row);
        this.row = null;
        return this;
    }

    public ReplyKeyboardBuilder button(String text) {
        row.add(text);
        return this;
    }



    public SendMessage build() {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        return message;
    }
}
