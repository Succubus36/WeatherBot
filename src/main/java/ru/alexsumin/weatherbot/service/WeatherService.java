package ru.alexsumin.weatherbot.service;

import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.WeatherState;

public interface WeatherService {

    WeatherState findWeatherByCity(String cityName);

    WeatherStatus getCurrentWeatherStatus(String cityName) throws Exception;
}
