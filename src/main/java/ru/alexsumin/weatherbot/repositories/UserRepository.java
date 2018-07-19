package ru.alexsumin.weatherbot.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
