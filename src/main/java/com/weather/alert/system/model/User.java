package com.weather.alert.system.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    private String name;
    private String phoneNumber;
    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;
    
    @Builder.Default
    private Set<AlertType> alertPreferences = Set.of(AlertType.SEVERE_WEATHER, AlertType.STORM);
    
    private List<String> locations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
