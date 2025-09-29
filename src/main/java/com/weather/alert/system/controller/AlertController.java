package com.weather.alert.system.controller;

import com.weather.alert.system.dto.AlertDto;
import com.weather.alert.system.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlertDto>> getUserAlerts(@PathVariable String userId) {
        List<AlertDto> alerts = alertService.getUserAlerts(userId);
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/check")
    public ResponseEntity<String> triggerWeatherCheck() {
        alertService.checkWeatherAlertsForAllUsers();
        return ResponseEntity.ok("Weather alert check triggered successfully");
    }

    @PostMapping("/cleanup")
    public ResponseEntity<String> cleanupExpiredAlerts() {
        alertService.cleanupExpiredAlerts();
        return ResponseEntity.ok("Expired alerts cleanup completed");
    }
}
