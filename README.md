# WeatherBot
>Это простой телеграм-бот, отправляющий оповещения об изменениях погоды в указанном городе. 

[![CircleCI](https://circleci.com/gh/alexsumin/WeatherBot.svg?style=svg)](https://circleci.com/gh/alexsumin/WeatherBot)


### Использованные технологии

* Java 8
* [Telegram Spring Boot Starter](https://github.com/xabgesagtx/telegram-spring-boot-starter)
* [Apache Maven](https://maven.apache.org/)
* [Spring Data JPA](https://projects.spring.io/spring-data-jpa/)
* [H2 database](http://www.h2database.com)
* [Project Lombok](https://projectlombok.org/)
* [Java Library for OpenWeatherMap.org Weather APIs](https://bitbucket.org/aksinghnet/owm-japis)
* [CircleCI](https://circleci.com/)
* [Junit](https://junit.org/junit4/)
* [Mockito](http://site.mockito.org/)

### Как пользоваться

1. Нужно склонировать этот проект
2. Создать нового бота в телеграм. Для этого найти [@BotFather](http://t.me/BotFather) и начать диалог с ним. Отправить ему комманду /newbot и следовать инструкции. После этого можно использовать полученные token и username. Изменить соответствующе значения в файле application.properties.
3. Зарегистрироваться на сайте [OpenWeatherMap](https://openweathermap.org/) и получить key. В файле application.properties изменить соответствующее поле weathermap.api.key.
4. Теперь выполнить команду 
`mvn clean install` 
и затем команду 
`java -jar weatherbot-0.0.1-SNAPSHOT.jar` для запуска


### Тестирование
Для запуска тестов следует использовать команду `mvn verify`

### Скриншоты
<img src="https://github.com/alexsumin/weatherbot/raw/master/screenshots/1.jpg" alt="alt text" width="400" height="711">
<img src="https://github.com/alexsumin/weatherbot/raw/master/screenshots/2.jpg" alt="alt text" width="400" height="711">
<img src="https://github.com/alexsumin/weatherbot/raw/master/screenshots/3.jpg" alt="alt text" width="400" height="711">
<img src="https://github.com/alexsumin/weatherbot/raw/master/screenshots/4.jpg" alt="alt text" width="400" height="711">
