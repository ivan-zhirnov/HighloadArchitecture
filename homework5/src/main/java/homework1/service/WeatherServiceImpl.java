package homework1.service;

import homework1.service.WeatherService;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${openweather.key}")
    private String apiKey;
    @Value("${openweather.url}")
    private String url;

    @Value("${redis.port}")
    private int port;
    @Value("${redis.host}")
    private String host;

    private final Jedis redis;

    @Autowired
    WeatherServiceImpl(@Value("${redis.port}") int port, @Value("${redis.host}") String host) {
        redis = new Jedis(host, port);
    }

    @Override
    public Map<String, Object> getCurrentWeather(String city) {
        String requestString = url + "weather?" +
                "q=" +
                city +
                "&appid=" +
                apiKey;
        Request request = new Request.Builder()
                .url(requestString)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try(Response response = httpClient.newCall(request).execute()) {
            Map<String, Object> result = new HashMap<>();
            JSONObject jsonBody = (JSONObject) new JSONParser().parse(Objects.requireNonNull(response.body()).string());
            result.put("temperature", getCelsiusFromKelvin(((JSONObject) jsonBody.get("main")).get("temp").toString()));
            result.put("unit", "celsius");
            result.put("city", city);
            return result;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getForecastWeather(String city, Date date) {
        String requestString = url + "forecast?" +
                "q=" +
                city +
                "&appid=" +
                apiKey +
                "&cnt=20";
        Request request = new Request.Builder()
                .url(requestString)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try(Response response= httpClient.newCall(request).execute()) {
            Map<String, Object> result = new HashMap<>();
            JSONObject jsonBody = (JSONObject) new JSONParser().parse(Objects.requireNonNull(response.body()).string());
            JSONObject correctJson = getJsonObjectByDate((JSONArray) jsonBody.get("list"), date);
            result.put("temperature", getCelsiusFromKelvin(((JSONObject) Objects.requireNonNull(correctJson).get("main")).get("temp").toString()));
            result.put("unit", "celsius");
            result.put("city", city);
            return result;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveWeather(String city, String temperature) {
        redis.publish(city, temperature);
        redis.flushAll();
        System.out.println(redis.get(city));
    }

    private JSONObject getJsonObjectByDate(JSONArray jsonArray, Date date) {
        Iterator iterator = jsonArray.iterator();
        JSONObject lastObject = null;

        while (iterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            Date jsonDate = new Date((long) jsonObject.get("dt")*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (sdf.format(jsonDate).equals(sdf.format(date))) {
                return jsonObject;
            }
            lastObject = jsonObject;
        }

        return lastObject;
    }

    private double getCelsiusFromKelvin(String kelvin) {
        return BigDecimal.valueOf(Float.parseFloat(kelvin) - 273).setScale(2, RoundingMode.DOWN).doubleValue();
    }
}
