package com.weather.alert.system.service;

import com.weather.alert.system.dto.WeatherConditionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    private Map<String, Object> mockWeatherResponse;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(weatherService, "baseUrl", "https://api.test.com");
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);

        // Mock weather API response
        mockWeatherResponse = Map.of(
            "main", Map.of(
                "temp", 25.5,
                "humidity", 60.0,
                "pressure", 1013.0
            ),
            "wind", Map.of(
                "speed", 10.5,
                "deg", 180
            ),
            "weather", List.of(Map.of(
                "main", "Clear",
                "description", "clear sky"
            )),
            "visibility", 10000
        );
    }

    @Test
    void getCurrentWeather_Success() {
        // Given
        String location = "London";
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(mockWeatherResponse);

        // When
        Optional<WeatherConditionDto> result = weatherService.getCurrentWeather(location);

        // Then
        assertTrue(result.isPresent());
        WeatherConditionDto weather = result.get();
        assertEquals(location, weather.getLocation());
        assertEquals(25.5, weather.getTemperature());
        assertEquals(60.0, weather.getHumidity());
        assertEquals(10.5, weather.getWindSpeed());
        assertEquals("Clear", weather.getMainCondition());
        assertEquals("clear sky", weather.getDescription());

        verify(restTemplate).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void getCurrentWeather_ApiError_ReturnsEmpty() {
        // Given
        String location = "InvalidLocation";
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("API Error"));

        // When
        Optional<WeatherConditionDto> result = weatherService.getCurrentWeather(location);

        // Then
        assertFalse(result.isPresent());
        verify(restTemplate).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void getWeatherAlerts_SevereWeather_ReturnsAlert() {
        // Given
        String location = "StormCity";
        Map<String, Object> severeWeatherResponse = Map.of(
            "main", Map.of(
                "temp", 15.0,
                "humidity", 85.0,
                "pressure", 980.0
            ),
            "wind", Map.of(
                "speed", 30.0,
                "deg", 270
            ),
            "weather", List.of(Map.of(
                "main", "Thunderstorm",
                "description", "severe thunderstorm"
            )),
            "visibility", 5000
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(severeWeatherResponse);

        // When
        Optional<WeatherConditionDto> result = weatherService.getWeatherAlerts(location);

        // Then
        assertTrue(result.isPresent());
        WeatherConditionDto weather = result.get();
        assertEquals("Thunderstorm", weather.getMainCondition());
        assertEquals("severe thunderstorm", weather.getDescription());
        assertEquals(30.0, weather.getWindSpeed());

        verify(restTemplate).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void getWeatherAlerts_NormalWeather_ReturnsEmpty() {
        // Given
        String location = "PeacefulCity";
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(mockWeatherResponse);

        // When
        Optional<WeatherConditionDto> result = weatherService.getWeatherAlerts(location);

        // Then
        assertFalse(result.isPresent());
        verify(restTemplate).getForObject(anyString(), eq(Map.class));
    }
}
