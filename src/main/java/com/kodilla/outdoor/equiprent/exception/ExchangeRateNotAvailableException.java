package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class ExchangeRateNotAvailableException extends Exception {
    public ExchangeRateNotAvailableException() {
        super();
    }
}
