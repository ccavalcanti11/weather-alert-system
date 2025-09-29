package com.weather.alert.system.service;

import com.weather.alert.system.dto.AlertDto;
import com.weather.alert.system.dto.WeatherConditionDto;
import com.weather.alert.system.model.*;
import com.weather.alert.system.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserService userService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AlertService alertService;

    private User testUser;
    private Alert testAlert;
    private WeatherConditionDto severeWeatherCondition;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user1")
                .email("test@example.com")
                .name("Test User")
                .phoneNumber("+1234567890")
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .alertPreferences(Set.of(AlertType.SEVERE_WEATHER, AlertType.STORM))
                .locations(List.of("London", "New York"))
                .build();

        testAlert = Alert.builder()
                .id("alert1")
                .userId("user1")
                .alertType(AlertType.STORM)
                .location("London")
                .title("Storm Alert for London")
                .message("Severe storm detected")
                .severity(Alert.AlertSeverity.HIGH)
                .status(Alert.AlertStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        severeWeatherCondition = WeatherConditionDto.builder()
                .location("London")
                .temperature(15.0)
                .humidity(85.0)
                .windSpeed(35.0)
                .mainCondition("Thunderstorm")
                .description("severe thunderstorm")
                .build();
    }

    @Test
    void checkWeatherAlertsForAllUsers_Success() {
        // Given
        when(userService.getAllUsers()).thenReturn(List.of(testUser));
        when(weatherService.getWeatherAlerts("London")).thenReturn(Optional.of(severeWeatherCondition));
        when(weatherService.getWeatherAlerts("New York")).thenReturn(Optional.empty());
        when(alertRepository.findByLocationAndAlertTypeAndCreatedAtAfter(anyString(), any(), any()))
                .thenReturn(List.of());
        when(alertRepository.save(any(Alert.class))).thenReturn(testAlert);
        when(notificationService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
        when(notificationService.sendSms(anyString(), anyString())).thenReturn(true);
        when(notificationService.formatWeatherAlert(anyString(), anyString(), anyString()))
                .thenReturn("Test alert message");

        // When
        alertService.checkWeatherAlertsForAllUsers();

        // Then
        verify(userService).getAllUsers();
        verify(weatherService).getWeatherAlerts("London");
        verify(weatherService).getWeatherAlerts("New York");
        verify(alertRepository, atLeastOnce()).save(any(Alert.class));
        verify(notificationService).sendEmail(eq("test@example.com"), anyString(), anyString());
        verify(notificationService).sendSms(eq("+1234567890"), anyString());
    }

    @Test
    void getUserAlerts_Success() {
        // Given
        when(alertRepository.findByUserIdOrderByCreatedAtDesc("user1"))
                .thenReturn(List.of(testAlert));

        // When
        List<AlertDto> result = alertService.getUserAlerts("user1");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        AlertDto alertDto = result.get(0);
        assertEquals("alert1", alertDto.getId());
        assertEquals("user1", alertDto.getUserId());
        assertEquals(AlertType.STORM, alertDto.getAlertType());
        assertEquals("London", alertDto.getLocation());

        verify(alertRepository).findByUserIdOrderByCreatedAtDesc("user1");
    }

    @Test
    void cleanupExpiredAlerts_Success() {
        // Given
        Alert expiredAlert = Alert.builder()
                .id("expired1")
                .status(Alert.AlertStatus.PENDING)
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();

        when(alertRepository.findByStatusAndExpiresAtBefore(eq(Alert.AlertStatus.PENDING), any()))
                .thenReturn(List.of(expiredAlert));
        when(alertRepository.save(any(Alert.class))).thenReturn(expiredAlert);

        // When
        alertService.cleanupExpiredAlerts();

        // Then
        verify(alertRepository).findByStatusAndExpiresAtBefore(eq(Alert.AlertStatus.PENDING), any());
        verify(alertRepository).save(argThat(alert ->
            alert.getStatus() == Alert.AlertStatus.EXPIRED));
    }

    @Test
    void convertToDto_Success() {
        // When
        AlertDto result = alertService.convertToDto(testAlert);

        // Then
        assertNotNull(result);
        assertEquals("alert1", result.getId());
        assertEquals("user1", result.getUserId());
        assertEquals(AlertType.STORM, result.getAlertType());
        assertEquals("London", result.getLocation());
        assertEquals("Storm Alert for London", result.getTitle());
        assertEquals(Alert.AlertSeverity.HIGH, result.getSeverity());
        assertEquals(Alert.AlertStatus.SENT, result.getStatus());
    }
}
