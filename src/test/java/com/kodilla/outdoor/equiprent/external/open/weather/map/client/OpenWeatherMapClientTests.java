package com.kodilla.outdoor.equiprent.external.open.weather.map.client;

import com.kodilla.outdoor.equiprent.exception.WeatherForecastNotAvailableException;
import com.kodilla.outdoor.equiprent.dto.WeatherMapDto;
import com.kodilla.outdoor.equiprent.external.open.weather.map.config.OpenWeatherMapConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherMapClientTests {
    @InjectMocks
    private OpenWeatherMapClient openWeatherMapClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OpenWeatherMapConfig openWeatherMapConfig;

    @Test
    public void shouldReturnWeatherForecast() throws WeatherForecastNotAvailableException {
        // Given
        double lat = 44.34;
        double lon = 10.99;
        WeatherMapDto mockResponse = new WeatherMapDto();

        URI expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/forecast")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", "1234")
                .queryParam("units", "metric")
                .build()
                .encode()
                .toUri();

        Mockito.when(openWeatherMapConfig.getApiEndpoint()).thenReturn("https://api.openweathermap.org/data/2.5/forecast");
        Mockito.when(openWeatherMapConfig.getApiKey()).thenReturn("1234");

        Mockito.when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(WeatherMapDto.class))).thenReturn(mockResponse);
        // When
        WeatherMapDto result = openWeatherMapClient.getWeatherForecast(lat, lon);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockResponse, result);
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(expectedUri, WeatherMapDto.class);
    }

    @Test
    public void shouldThrowWeatherForecastNotAvailableException() {
        // Given
        double lat = 44.34;
        double lon = 10.99;

        URI expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/forecast")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", "1234")
                .queryParam("units", "metric")
                .build()
                .encode()
                .toUri();

        Mockito.when(openWeatherMapConfig.getApiEndpoint()).thenReturn("https://api.openweathermap.org/data/2.5/forecast");
        Mockito.when(openWeatherMapConfig.getApiKey()).thenReturn("1234");

        Mockito.when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(WeatherMapDto.class)))
                .thenThrow(new RestClientException("Error during API call"));
        // When & Then
        Assertions.assertThrows(WeatherForecastNotAvailableException.class, () -> {
            openWeatherMapClient.getWeatherForecast(lat, lon);
        });

        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(expectedUri, WeatherMapDto.class);
    }
}
