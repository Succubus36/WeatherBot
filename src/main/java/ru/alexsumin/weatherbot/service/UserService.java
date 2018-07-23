package ru.alexsumin.weatherbot.service;

import ru.alexsumin.weatherbot.domain.entity.User;

public interface UserService {

    User findById(Long id);

    User save(User user);

}
