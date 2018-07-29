package ru.alexsumin.weatherbot.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.repository.UserRepository;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void findByIdTest() {
        User user = new User();
        user.setId(1L);

        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);

        User foundUser = userService.findById(1L);

        assertEquals(Long.valueOf(1L), foundUser.getId());
        verify(userRepository, times(1)).findById(anyLong());

    }

    @Test
    public void saveTest() {
        User user = new User();
        user.setId(1L);

        Optional<User> optionalUser = Optional.of(user);

        User savedUser = new User();
        savedUser.setId(3L);

        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
        when(userRepository.save(any())).thenReturn(savedUser);

        User foundUser = userService.findById(1L);
        User anotherUser = userService.save(new User());

        assertEquals(Long.valueOf(1L), foundUser.getId());
        assertEquals(Long.valueOf(3L), anotherUser.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }
}