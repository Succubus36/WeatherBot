package ru.alexsumin.weatherbot.service;

import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.WeatherState;

public interface WeatherStateService {


    WeatherStatus getCurrentWeatherStatus(String cityName) throws Exception;

    WeatherState save(WeatherState state);


}
