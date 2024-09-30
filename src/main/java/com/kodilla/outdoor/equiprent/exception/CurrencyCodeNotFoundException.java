package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class CurrencyCodeNotFoundException extends Exception {
    private String currencyCode;

    public CurrencyCodeNotFoundException(String currencyCode) {
        super("Currency code " + currencyCode + " does not exist.");
        this.currencyCode = currencyCode;
    }
}
