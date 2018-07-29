package ru.alexsumin.weatherbot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.concurrent.Callable;

public abstract class Command implements Callable<SendMessage> {

    @Override
    public abstract SendMessage call();
}
