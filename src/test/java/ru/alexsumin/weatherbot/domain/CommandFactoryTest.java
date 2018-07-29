package ru.alexsumin.weatherbot.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.api.objects.Message;
import ru.alexsumin.weatherbot.commands.ChangeCityCommand;
import ru.alexsumin.weatherbot.commands.Command;
import ru.alexsumin.weatherbot.commands.StartCommand;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.service.SubscriptionService;
import ru.alexsumin.weatherbot.service.UserService;
import ru.alexsumin.weatherbot.service.WeatherService;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CommandFactoryTest {
    @Mock
    UserService userService;
    @Mock
    SubscriptionService subscriptionService;
    @Mock
    WeatherService weatherService;
    @Mock
    Message message;

    CommandFactory commandFactory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        commandFactory = new CommandFactory(userService, subscriptionService, weatherService);
    }

    @Test
    public void getCommand() {
        when(message.getChatId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        user.setCurrentMenu(CurrentMenu.CHANGE_CITY);

        when(userService.findById(anyLong())).thenReturn(user);

        Command command = commandFactory.getCommand(message);

        assertEquals(command.getClass(), ChangeCityCommand.class);
        assertNotEquals(command.getClass(), StartCommand.class);
    }
}