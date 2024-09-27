package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class WeatherForecastNotAvailableException extends Exception {
    public WeatherForecastNotAvailableException() {
        super();
    }
}
