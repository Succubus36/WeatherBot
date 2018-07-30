package ru.alexsumin.weatherbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexsumin.weatherbot.domain.entity.User;
import ru.alexsumin.weatherbot.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        Optional<User> account = userRepository.findById(id);
        return account.orElseGet(() -> registerUser(id));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    private User registerUser(Long chatId) {
        log.info("Registering new user with id:" + chatId);
        User newUser = new User(chatId);
        userRepository.save(newUser);
        return newUser;
    }


}
