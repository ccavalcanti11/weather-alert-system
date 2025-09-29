package com.weather.alert.system.repository;

import com.weather.alert.system.model.Alert;
import com.weather.alert.system.model.AlertType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Alert> findByLocationAndAlertTypeAndCreatedAtAfter(String location, AlertType alertType, LocalDateTime after);
    List<Alert> findByStatusAndExpiresAtBefore(Alert.AlertStatus status, LocalDateTime expiredBefore);
    long countByUserIdAndCreatedAtAfter(String userId, LocalDateTime after);
}
