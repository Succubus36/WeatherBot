package ru.alexsumin.weatherbot.service;

import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.alexsumin.weatherbot.domain.NewCityEvent;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
public class WeatherServiceImp implements WeatherService {

    private final SubscriptionService subscriptionService;

    private Set<String> cities;
    private Map<String, List<WeatherData>> forecasts;

    public WeatherServiceImp(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Value("${weathermap.api.key}")
    private String key;

    @PostConstruct
    private void init() {
        log.info("Initializing weather service");
        updateCities();
        updateForecast();
    }

    private void updateCities() {
        log.info("Updating cities list");
        cities = new HashSet<>();
        List<Subscription> subscriptions = subscriptionService.getActiveSubscriptions();
        if (!subscriptions.isEmpty()) {
            subscriptions.forEach(subscription -> {
                cities.add(subscription.getCity());
            });
        }
    }

    @TransactionalEventListener
    private void addNewCity(NewCityEvent event) {
        log.info("Add new city: " + event.getCity());
        String newCity = event.getCity();
        if (!cities.contains(newCity)) {
            updateCities();
            updateForecast();
        }
    }

    public List<WeatherData> getForecastByCity(String city) {
        return forecasts.get(city);
    }

    public WeatherStatus getCurrentWeatherStatus(String cityName) throws Exception {
        OWM owm = new OWM(key);
        CurrentWeather cw = owm.currentWeatherByCityName(cityName);
        return WeatherStatus.getStatus(cw.getWeatherList().get(0).getMainInfo());
    }

    private void updateForecast() {
        OWM owm = new OWM(key);
        Map<String, List<WeatherData>> forecastTemp = new HashMap<>();
        boolean isSuccessUpdate = true;
        if (!cities.isEmpty()) {
            for (String city : cities) {
                try {
                    HourlyWeatherForecast forecast = owm.hourlyWeatherForecastByCityName(city);
                    forecastTemp.put(city, forecast.getDataList());
                } catch (APIException e) {
                    log.error("Couldn't update forecast");
                    log.error(e.getMessage());
                    isSuccessUpdate = false;
                }
            }
        }
        if (isSuccessUpdate) forecasts = forecastTemp;
        log.info("Forecast was updated");
    }

    @Scheduled(cron = "0 1 0/3 * * *")
    public void everyThreeHoursUpdate() {
        log.info("Every three hours task: updating forecasts");
        updateForecast();
    }
}
