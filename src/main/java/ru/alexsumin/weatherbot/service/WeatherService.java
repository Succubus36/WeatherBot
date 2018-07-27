package ru.alexsumin.weatherbot.service;

import net.aksingh.owmjapis.model.param.WeatherData;
import ru.alexsumin.weatherbot.domain.WeatherStatus;

import java.util.List;

public interface WeatherService {

    List<WeatherData> getForecastByCity(String city);

    WeatherStatus getCurrentWeatherStatus(String cityName) throws Exception;
}
