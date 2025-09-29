package com.weather.alert.system.controller;

import com.weather.alert.system.dto.WeatherConditionDto;
import com.weather.alert.system.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/current/{location}")
    public ResponseEntity<WeatherConditionDto> getCurrentWeather(@PathVariable String location) {
        Optional<WeatherConditionDto> weather = weatherService.getCurrentWeather(location);
        return weather.map(w -> ResponseEntity.ok(w))
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/alerts/{location}")
    public ResponseEntity<WeatherConditionDto> getWeatherAlerts(@PathVariable String location) {
        Optional<WeatherConditionDto> alerts = weatherService.getWeatherAlerts(location);
        return alerts.map(a -> ResponseEntity.ok(a))
                    .orElse(ResponseEntity.noContent().build());
    }
}
