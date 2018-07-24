package ru.alexsumin.weatherbot.service;

import org.springframework.stereotype.Service;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.WeatherState;
import ru.alexsumin.weatherbot.repository.WeatherStateRepository;

@Service
public class WeatherStateServiceImpl implements WeatherStateService {

    private WeatherStateRepository weatherStateRepository;
    private WeatherService weatherService;

    public WeatherStateServiceImpl(WeatherStateRepository weatherStateRepository, WeatherService weatherService) {
        this.weatherStateRepository = weatherStateRepository;
        this.weatherService = weatherService;
    }

    @Override
    public WeatherStatus getCurrentWeatherStatus(String cityName) throws Exception {
        return weatherService.getCurrentWeatherStatus(cityName);
    }

    @Override
    public WeatherState save(WeatherState state) {
        return weatherStateRepository.save(state);
    }
}
