package ru.alexsumin.weatherbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
