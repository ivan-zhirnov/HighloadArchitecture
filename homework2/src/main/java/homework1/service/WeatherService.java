package homework1.service;

import java.util.Date;
import java.util.Map;

public interface WeatherService {
    Map<String, Object> getCurrentWeather(String city);

    Map<String, Object> getForecastWeather(String city, Date date);
}
