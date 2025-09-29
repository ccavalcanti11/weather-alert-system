package com.weather.alert.system.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String userId;
    private AlertType alertType;
    private String location;
    private String title;
    private String message;
    private AlertSeverity severity;
    private AlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime expiresAt;
    private WeatherCondition weatherCondition;

    public enum AlertSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum AlertStatus {
        PENDING, SENT, FAILED, EXPIRED
    }
}
