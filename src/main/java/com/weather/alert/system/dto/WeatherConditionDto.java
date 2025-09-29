package com.weather.alert.system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherConditionDto {

    private String location;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private String windDirection;
    private double pressure;
    private String description;
    private String mainCondition;
    private double visibility;
    private double uvIndex;
    private double precipitationChance;
}
