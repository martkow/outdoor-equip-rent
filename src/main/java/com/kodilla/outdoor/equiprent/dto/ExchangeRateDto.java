package com.kodilla.outdoor.equiprent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rate {
        @JsonProperty("no")
        private String exchangeRateNumber;
        private String effectiveDate;
        @JsonProperty("mid")
        private double averageExchangeRate;
    }
}