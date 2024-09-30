package com.kodilla.outdoor.equiprent.external.open.weather.map.client;

import com.kodilla.outdoor.equiprent.exception.WeatherForecastNotAvailableException;
import com.kodilla.outdoor.equiprent.dto.WeatherMapDto;
import com.kodilla.outdoor.equiprent.external.open.weather.map.config.OpenWeatherMapConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class OpenWeatherMapClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherMapClient.class);
    private final OpenWeatherMapConfig openWeatherMapConfig;
    private final RestTemplate restTemplate;

    public WeatherMapDto getWeatherForecast(double lat, double lon) throws WeatherForecastNotAvailableException {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(openWeatherMapConfig.getApiEndpoint())
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("appid", openWeatherMapConfig.getApiKey())
                    .queryParam("units", "metric")
                    .build()
                    .encode()
                    .toUri();

            return restTemplate.getForObject(url, WeatherMapDto.class);
        } catch (RestClientException rce) {
            LOGGER.error(rce.getMessage(), rce);
            throw new WeatherForecastNotAvailableException();
        }
    }
}