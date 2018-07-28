package ru.alexsumin.weatherbot.service;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
    }

    private void updateCities() {
        cities = new HashSet<>();
        List<Subscription> subscriptions = subscriptionService.getActiveSubscriptions();
        if (!subscriptions.isEmpty())
            subscriptions.forEach(subscription -> cities.add(subscription.getCity()));
    }

    @EventListener
    private void addNewCity(NewCityEvent event) {
        System.out.println("new city event!!");
        String newCity = event.getCity();
        if (!cities.contains(newCity)){
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

    private void updateForecast(){
        OWM owm = new OWM(key);
        Map<String, List<WeatherData>> forecastTemp = new HashMap<>();
        boolean isSuccessUpdate = true;
        if (!cities.isEmpty())
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
        if (isSuccessUpdate) forecasts = forecastTemp;
    }

    //@Scheduled(cron = "0 0 0,12 * * *")
    @Scheduled(cron = "0 0/5 * * * *")
    public void twiceInDayUpdate() {
        System.out.println(dateFormat.format(new Date(System.currentTimeMillis())));
        System.out.println("every 12 hours task!!!");
        updateForecast();
    }
}
