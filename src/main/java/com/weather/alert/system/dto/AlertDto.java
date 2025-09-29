package com.weather.alert.system.dto;

import com.weather.alert.system.model.Alert;
import com.weather.alert.system.model.AlertType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AlertDto {

    private String id;
    private String userId;
    private AlertType alertType;
    private String location;
    private String title;
    private String message;
    private Alert.AlertSeverity severity;
    private Alert.AlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime expiresAt;
    private WeatherConditionDto weatherCondition;
}
