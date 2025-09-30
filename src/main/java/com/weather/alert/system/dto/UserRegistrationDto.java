package com.weather.alert.system.dto;

import com.weather.alert.system.model.AlertType;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserRegistrationDto {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    private String phoneNumber;

    @NotNull(message = "Email notifications preference is required")
    private boolean emailNotificationsEnabled;

    @NotNull(message = "SMS notifications preference is required")
    private boolean smsNotificationsEnabled;

    private Set<AlertType> alertPreferences;
    private List<String> locations;
}
