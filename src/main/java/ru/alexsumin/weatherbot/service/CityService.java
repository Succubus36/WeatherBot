package ru.alexsumin.weatherbot.service;

import ru.alexsumin.weatherbot.domain.entity.City;

public interface CityService {

    boolean checkForExist(String name);

    City findByName(String name);

    City save(City city);

}
