package ru.alexsumin.weatherbot.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.alexsumin.weatherbot.domain.City;

public interface CityRepository extends CrudRepository<City, Long> {
}
