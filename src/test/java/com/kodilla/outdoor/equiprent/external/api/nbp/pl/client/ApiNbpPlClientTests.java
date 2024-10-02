package com.kodilla.outdoor.equiprent.external.api.nbp.pl.client;

import com.kodilla.outdoor.equiprent.dto.ExchangeRateDto;
import com.kodilla.outdoor.equiprent.dto.WeatherMapDto;
import com.kodilla.outdoor.equiprent.exception.ExchangeRateNotAvailableException;
import com.kodilla.outdoor.equiprent.exception.WeatherForecastNotAvailableException;
import com.kodilla.outdoor.equiprent.external.api.nbp.pl.config.ApiNbpPlConfig;
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
public class ApiNbpPlClientTests {
    @InjectMocks
    private ApiNbpPlClient apiNbpPlClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiNbpPlConfig apiNbpPlConfig;

    @Test
    public void shouldReturnEuroExchangeRate() throws ExchangeRateNotAvailableException {
        // Given
        ExchangeRateDto mockResponse = new ExchangeRateDto();

        URI expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.nbp.pl/api/exchangerates/rates/A/EUR/")
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();

        Mockito.when(apiNbpPlConfig.getApiEndpoint()).thenReturn("https://api.nbp.pl/api/");

        Mockito.when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(ExchangeRateDto.class))).thenReturn(mockResponse);
        // When
        ExchangeRateDto result = apiNbpPlClient.getEuroExchangeRate();
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockResponse, result);
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(expectedUri, ExchangeRateDto.class);
    }

    @Test
    public void shouldThrowExchangeRateNotAvailableException() {
        // Given
        URI expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.nbp.pl/api/exchangerates/rates/A/EUR/")
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();

        Mockito.when(apiNbpPlConfig.getApiEndpoint()).thenReturn("https://api.nbp.pl/api/");

        Mockito.when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(ExchangeRateDto.class)))
                .thenThrow(new RestClientException("Error during API call"));
        // When & Then
        Assertions.assertThrows(ExchangeRateNotAvailableException.class, () -> apiNbpPlClient.getEuroExchangeRate());

        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(expectedUri, ExchangeRateDto.class);
    }
}
