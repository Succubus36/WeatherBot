package ru.alexsumin.weatherbot.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alexsumin.weatherbot.domain.WeatherStatus;
import ru.alexsumin.weatherbot.domain.entity.Subscription;
import ru.alexsumin.weatherbot.domain.entity.User;


import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryIT {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    private String city1 = "Moscow";
    private String city2 = "New York City";
    private String city3 = "Paris";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findAllActiveTest(){
        User user = new User(1L);
        userRepository.save(user);
        Subscription subscription = new Subscription(user, WeatherStatus.DRIZZLE, city1);
        subscription.setActive(true);
        subscription.setTimeToAlert(1);
        subscriptionRepository.save(subscription);

        User user2 = new User(2L);
        userRepository.save(user2);
        Subscription subscription2 = new Subscription(user2, WeatherStatus.DRIZZLE, city2);
        subscription2.setActive(true);
        subscription2.setTimeToAlert(1);
        subscriptionRepository.save(subscription2);

        User user3 = new User(3L);
        userRepository.save(user3);
        Subscription subscription3 = new Subscription(user3, WeatherStatus.DRIZZLE, city3);
        subscription3.setActive(false);
        subscription3.setTimeToAlert(1);
        subscriptionRepository.save(subscription3);


        List<Subscription> activeSubs = subscriptionRepository.findAllByIsActiveTrue();
        assertEquals(2, activeSubs.size());
        assertEquals(city1, activeSubs.get(0).getCity());
        assertEquals(city2, activeSubs.get(1).getCity());
    }

    @Test
    public void findAllActiveEmptyTest(){

        List<Subscription> activeSubs = subscriptionRepository.findAllByIsActiveTrue();
        assertEquals(Collections.emptyList(), activeSubs);
    }
}