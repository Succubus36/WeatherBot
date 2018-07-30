package ru.alexsumin.weatherbot.service;

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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherServiceImp implements WeatherService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
        updateCities();
        updateForecast();
    }

    private void updateCities() {
        System.out.println("обновляю список городов");
        cities = new HashSet<>();
        List<Subscription> subscriptions = subscriptionService.getActiveSubscriptions();
        if (!subscriptions.isEmpty()) {
            subscriptions.forEach(subscription -> {
                System.out.println("добавил город: " + subscription.getCity());
                cities.add(subscription.getCity());
            });
        }
    }

    @TransactionalEventListener
    private void addNewCity(NewCityEvent event) {
        System.out.println("new city event!!");
        System.out.println(event.getCity());
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
            for (String s : cities) {
                try {
                    HourlyWeatherForecast forecast = owm.hourlyWeatherForecastByCityName(s);
                    forecastTemp.put(s, forecast.getDataList());
                } catch (APIException e) {
                    e.printStackTrace();
                    isSuccessUpdate = false;
                    System.out.println("проблема с обновлением");
                }
            }
        }
        if (isSuccessUpdate) forecasts = forecastTemp;
        System.out.println("обновил прогноз!");
    }

    @Scheduled(cron = "0 1 0/3 * * *")
    public void everyThreeHoursUpdate() {
        System.out.println(dateFormat.format(new Date(System.currentTimeMillis())));
        System.out.println("every 3 hours task!!!");
        updateForecast();
    }
}
