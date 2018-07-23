package ru.alexsumin.weatherbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.entity.City;

public interface CityRepository extends CrudRepository<City, Long> {
}
