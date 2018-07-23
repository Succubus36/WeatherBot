package ru.alexsumin.weatherbot.service;

import org.springframework.stereotype.Service;
import ru.alexsumin.weatherbot.domain.entity.City;
import ru.alexsumin.weatherbot.repository.CityRepository;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public boolean checkForExist(String name) {
        return false;
    }

    @Override
    public City findByName(String name) {
        return null;
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }
}
