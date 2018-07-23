package ru.alexsumin.weatherbot.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.concurrent.Callable;

public abstract class MessageHandler implements Callable<SendMessage> {

    @Override
    public abstract SendMessage call() throws Exception;
}
