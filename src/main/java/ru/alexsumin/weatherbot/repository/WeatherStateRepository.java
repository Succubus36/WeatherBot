package ru.alexsumin.weatherbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.entity.WeatherState;

public interface WeatherStateRepository extends CrudRepository<WeatherState, Long> {
}
