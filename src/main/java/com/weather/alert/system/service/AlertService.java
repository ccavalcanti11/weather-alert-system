package com.weather.alert.system.service;

import com.weather.alert.system.dto.AlertDto;
import com.weather.alert.system.dto.WeatherConditionDto;
import com.weather.alert.system.model.*;
import com.weather.alert.system.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserService userService;
    private final WeatherService weatherService;
    private final NotificationService notificationService;

    @Scheduled(fixedRateString = "${weather.alert.schedule.fixed-rate}",
               initialDelayString = "${weather.alert.schedule.initial-delay}")
    public void checkWeatherAlertsForAllUsers() {
        log.info("Starting scheduled weather alert check for all users");

        List<User> users = userService.getAllUsers();

        users.parallelStream().forEach(user -> {
            if (user.getLocations() != null && !user.getLocations().isEmpty()) {
                user.getLocations().forEach(location -> checkWeatherForLocation(user, location));
            }
        });

        log.info("Completed scheduled weather alert check for {} users", users.size());
    }

    private void checkWeatherForLocation(User user, String location) {
        try {
            Optional<WeatherConditionDto> weatherAlert = weatherService.getWeatherAlerts(location);

            if (weatherAlert.isPresent()) {
                WeatherConditionDto condition = weatherAlert.get();
                AlertType alertType = determineAlertType(condition);

                if (user.getAlertPreferences().contains(alertType)) {
                    createAndSendAlert(user, location, condition, alertType);
                }
            }
        } catch (Exception e) {
            log.error("Error checking weather for user {} and location {}: {}",
                     user.getEmail(), location, e.getMessage());
        }
    }

    private AlertType determineAlertType(WeatherConditionDto condition) {
        String mainCondition = condition.getMainCondition().toLowerCase();

        return switch (mainCondition) {
            case "thunderstorm" -> AlertType.STORM;
            case "tornado" -> AlertType.TORNADO;
            case "hurricane" -> AlertType.HURRICANE;
            case "snow" -> AlertType.SNOW;
            case "rain" -> condition.getWindSpeed() > 20 ? AlertType.HIGH_WIND : AlertType.GENERAL_WARNING;
            default -> {
                if (condition.getTemperature() > 35) yield AlertType.HEATWAVE;
                if (condition.getWindSpeed() > 25) yield AlertType.HIGH_WIND;
                yield AlertType.SEVERE_WEATHER;
            }
        };
    }

    private void createAndSendAlert(User user, String location, WeatherConditionDto condition, AlertType alertType) {
        // Check if similar alert was sent recently to avoid spam
        LocalDateTime recentThreshold = LocalDateTime.now().minusHours(3);
        List<Alert> recentAlerts = alertRepository.findByLocationAndAlertTypeAndCreatedAtAfter(
            location, alertType, recentThreshold);

        if (!recentAlerts.isEmpty()) {
            log.debug("Skipping alert for user {} - similar alert sent recently", user.getEmail());
            return;
        }

        WeatherCondition weatherCondition = WeatherCondition.builder()
                .location(condition.getLocation())
                .temperature(condition.getTemperature())
                .humidity(condition.getHumidity())
                .windSpeed(condition.getWindSpeed())
                .windDirection(condition.getWindDirection())
                .pressure(condition.getPressure())
                .description(condition.getDescription())
                .mainCondition(condition.getMainCondition())
                .visibility(condition.getVisibility())
                .uvIndex(condition.getUvIndex())
                .precipitationChance(condition.getPrecipitationChance())
                .build();

        Alert alert = Alert.builder()
                .userId(user.getId())
                .alertType(alertType)
                .location(location)
                .title(String.format("%s Alert for %s", alertType.name().replace("_", " "), location))
                .message(createAlertMessage(condition, alertType))
                .severity(determineSeverity(condition, alertType))
                .status(Alert.AlertStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(6))
                .weatherCondition(weatherCondition)
                .build();

        Alert savedAlert = alertRepository.save(alert);
        sendNotifications(user, savedAlert);
    }

    private String createAlertMessage(WeatherConditionDto condition, AlertType alertType) {
        return String.format(
            "Weather Alert: %s detected in %s. " +
            "Current conditions: %s, Temperature: %.1f°C, Wind: %.1f km/h. " +
            "Please take necessary precautions.",
            alertType.name().replace("_", " "),
            condition.getLocation(),
            condition.getDescription(),
            condition.getTemperature(),
            condition.getWindSpeed()
        );
    }

    private Alert.AlertSeverity determineSeverity(WeatherConditionDto condition, AlertType alertType) {
        return switch (alertType) {
            case TORNADO, HURRICANE -> Alert.AlertSeverity.CRITICAL;
            case SEVERE_WEATHER, STORM, FLOOD -> Alert.AlertSeverity.HIGH;
            case HEATWAVE, HIGH_WIND -> Alert.AlertSeverity.MEDIUM;
            default -> Alert.AlertSeverity.LOW;
        };
    }

    private void sendNotifications(User user, Alert alert) {
        boolean emailSent = false;
        boolean smsSent = false;

        String message = notificationService.formatWeatherAlert(
            alert.getLocation(),
            alert.getAlertType().name(),
            alert.getMessage()
        );

        if (user.isEmailNotificationsEnabled()) {
            emailSent = notificationService.sendEmail(user.getEmail(), alert.getTitle(), message);
        }

        if (user.isSmsNotificationsEnabled() && user.getPhoneNumber() != null) {
            smsSent = notificationService.sendSms(user.getPhoneNumber(), message);
        }

        if (emailSent || smsSent) {
            alert.setStatus(Alert.AlertStatus.SENT);
            alert.setSentAt(LocalDateTime.now());
            log.info("Alert sent successfully to user {}: {}", user.getEmail(), alert.getTitle());
        } else {
            alert.setStatus(Alert.AlertStatus.FAILED);
            log.warn("Failed to send alert to user {}: {}", user.getEmail(), alert.getTitle());
        }

        alertRepository.save(alert);
    }

    public List<AlertDto> getUserAlerts(String userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AlertDto convertToDto(Alert alert) {
        return AlertDto.builder()
                .id(alert.getId())
                .userId(alert.getUserId())
                .alertType(alert.getAlertType())
                .location(alert.getLocation())
                .title(alert.getTitle())
                .message(alert.getMessage())
                .severity(alert.getSeverity())
                .status(alert.getStatus())
                .createdAt(alert.getCreatedAt())
                .sentAt(alert.getSentAt())
                .expiresAt(alert.getExpiresAt())
                .build();
    }

    @Scheduled(cron = "0 0 */6 * * *") // Every 6 hours
    public void cleanupExpiredAlerts() {
        List<Alert> expiredAlerts = alertRepository.findByStatusAndExpiresAtBefore(
            Alert.AlertStatus.PENDING, LocalDateTime.now());

        expiredAlerts.forEach(alert -> {
            alert.setStatus(Alert.AlertStatus.EXPIRED);
            alertRepository.save(alert);
        });

        log.info("Cleaned up {} expired alerts", expiredAlerts.size());
    }
}
