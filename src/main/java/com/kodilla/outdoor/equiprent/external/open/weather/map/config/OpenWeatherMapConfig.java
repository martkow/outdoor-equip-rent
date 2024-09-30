package com.kodilla.outdoor.equiprent.external.open.weather.map.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OpenWeatherMapConfig {
    @Value("${openweathermp.api.endpoint}")
    private String apiEndpoint;
    @Value("${openweathermap.api.key}")
    private String apiKey;
}
