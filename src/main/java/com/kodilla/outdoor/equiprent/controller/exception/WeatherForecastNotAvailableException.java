package com.kodilla.outdoor.equiprent.controller.exception;

import lombok.Getter;

@Getter
public class WeatherForecastNotAvailableException extends Exception {
    public WeatherForecastNotAvailableException() {
        super();
    }
}
