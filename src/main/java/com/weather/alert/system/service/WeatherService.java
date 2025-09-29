package com.weather.alert.system.service;

import com.weather.alert.system.dto.WeatherConditionDto;
import com.weather.alert.system.model.WeatherCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "weather", key = "#location")
    public Optional<WeatherConditionDto> getCurrentWeather(String location) {
        try {
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric", baseUrl, location, apiKey);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                return Optional.of(mapToWeatherConditionDto(response, location));
            }
        } catch (Exception e) {
            log.error("Error fetching weather data for location: {}", location, e);
        }

        return Optional.empty();
    }

    @Cacheable(value = "weather-alerts", key = "#location")
    public Optional<WeatherConditionDto> getWeatherAlerts(String location) {
        try {
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric", baseUrl, location, apiKey);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && isSevereWeather(response)) {
                return Optional.of(mapToWeatherConditionDto(response, location));
            }
        } catch (Exception e) {
            log.error("Error fetching weather alerts for location: {}", location, e);
        }

        return Optional.empty();
    }

    public WeatherCondition mapToWeatherCondition(Map<String, Object> response, String location) {
        @SuppressWarnings("unchecked")
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        @SuppressWarnings("unchecked")
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        @SuppressWarnings("unchecked")
        Map<String, Object> weather = ((java.util.List<Map<String, Object>>) response.get("weather")).get(0);

        return WeatherCondition.builder()
                .location(location)
                .temperature(((Number) main.get("temp")).doubleValue())
                .humidity(((Number) main.get("humidity")).doubleValue())
                .pressure(((Number) main.get("pressure")).doubleValue())
                .windSpeed(wind != null ? ((Number) wind.getOrDefault("speed", 0)).doubleValue() : 0)
                .windDirection(wind != null ? (String) wind.getOrDefault("deg", "N/A") : "N/A")
                .description((String) weather.get("description"))
                .mainCondition((String) weather.get("main"))
                .visibility(response.containsKey("visibility") ? ((Number) response.get("visibility")).doubleValue() : 0)
                .build();
    }

    private WeatherConditionDto mapToWeatherConditionDto(Map<String, Object> response, String location) {
        @SuppressWarnings("unchecked")
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        @SuppressWarnings("unchecked")
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        @SuppressWarnings("unchecked")
        Map<String, Object> weather = ((java.util.List<Map<String, Object>>) response.get("weather")).get(0);

        return WeatherConditionDto.builder()
                .location(location)
                .temperature(((Number) main.get("temp")).doubleValue())
                .humidity(((Number) main.get("humidity")).doubleValue())
                .pressure(((Number) main.get("pressure")).doubleValue())
                .windSpeed(wind != null ? ((Number) wind.getOrDefault("speed", 0)).doubleValue() : 0)
                .windDirection(wind != null ? String.valueOf(wind.getOrDefault("deg", "N/A")) : "N/A")
                .description((String) weather.get("description"))
                .mainCondition((String) weather.get("main"))
                .visibility(response.containsKey("visibility") ? ((Number) response.get("visibility")).doubleValue() : 0)
                .build();
    }

    private boolean isSevereWeather(Map<String, Object> response) {
        @SuppressWarnings("unchecked")
        Map<String, Object> weather = ((java.util.List<Map<String, Object>>) response.get("weather")).get(0);
        String condition = (String) weather.get("main");

        return condition.equalsIgnoreCase("Thunderstorm") ||
               condition.equalsIgnoreCase("Tornado") ||
               condition.equalsIgnoreCase("Hurricane") ||
               response.containsKey("alerts");
    }
}
