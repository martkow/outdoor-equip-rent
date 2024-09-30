package com.kodilla.outdoor.equiprent.external.api.nbp.pl.client;

import com.kodilla.outdoor.equiprent.dto.ExchangeRateDto;
import com.kodilla.outdoor.equiprent.exception.ExchangeRateNotAvailableException;
import com.kodilla.outdoor.equiprent.external.api.nbp.pl.config.ApiNbpPlConfig;
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
public class ApiNbpPlClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiNbpPlClient.class);
    private final ApiNbpPlConfig apiNbpPlConfig;
    private final RestTemplate restTemplate;

    public ExchangeRateDto getEuroExchangeRate() throws ExchangeRateNotAvailableException {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(apiNbpPlConfig.getApiEndpoint() + "/exchangerates/rates/A/EUR/")
                    .queryParam("format", "json")
                    .build()
                    .encode()
                    .toUri();

            return restTemplate.getForObject(url, ExchangeRateDto.class);
        } catch (RestClientException e) {
            LOGGER.error("Error while fetching exchange rate from NBP API: {}", e.getMessage(), e);
            throw new ExchangeRateNotAvailableException();
        }
    }
}
