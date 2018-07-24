package ru.alexsumin.weatherbot.service;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.WeatherState;
import ru.alexsumin.weatherbot.repository.WeatherStateRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WeatherServiceImp implements WeatherService {

    private final WeatherStateRepository weatherStateRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${weathermap.api.key}")
    private String key;

    public WeatherServiceImp(WeatherStateRepository weatherStateRepository) {
        this.weatherStateRepository = weatherStateRepository;
    }

    @Override
    public WeatherState findWeatherByCity(String cityName) {
        return null;
    }

    public WeatherStatus getCurrentWeatherStatus(String cityName) throws Exception {
        OWM owm = new OWM(key);
        CurrentWeather cw = owm.currentWeatherByCityName(cityName);
        return WeatherStatus.getStatus(cw.getWeatherList().get(0).getMainInfo());
    }

    @Scheduled(cron = "0 0 * * * *")
    public void everyHourTaskTest() {

        System.out.println(dateFormat.format(new Date(System.currentTimeMillis())));
        System.out.println("every hour task!!!");
    }
}
