package homework1.controller;

import homework1.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WeatherController extends BaseController {

    @Autowired
    private WeatherService weatherService;


    @RequestMapping(value = "current")
    @ResponseBody
    public Map<String, Object> getCurrentWeather(@RequestParam(value = "city") String city) {
        return weatherService.getCurrentWeather(city);
    }

    @RequestMapping(value = "forecast")
    @ResponseBody
    public Map<String, Object> getForecastWeather(@RequestParam(value = "city") String city,
                                                  @RequestParam(value = "dt") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
        return weatherService.getForecastWeather(city, date);
//        return new HashMap<>();
    }
}
