package com.kodilla.outdoor.equiprent.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {
    @AllArgsConstructor
    @Getter
    public static class Error {
        @Schema(description = "Level of the error, e.g., ERROR, WARNING, INFO")
        private String level;
        @Schema(description = "Unique code representing the error type")
        private String code;
        @Schema(description = "Detailed description of the error")
        private String description;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(CategoryNotFoundException.class)
    public Error handleCategoryNotFoundException(CategoryNotFoundException cnfe) {
        return new Error("ERROR", "category.does.not.exist", "Category " + cnfe.getCategory() + " not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(EquipmentNotFoundException.class)
    public Error handleEquipmentNotFoundException(EquipmentNotFoundException enfe) {
        return new Error("ERROR", "equipment.does.not.exist", "Equipment with ID " + enfe.getEquipmentId() + " not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(EquipmentNotAvailableException.class)
    public Error handleEquipmentNotAvailableException(EquipmentNotAvailableException enae) {
        return new Error("ERROR", "equipment.not.available", "Equipment with ID " + enae.getEquipmentId() + " not available.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ActiveEquipmentRentalException.class)
    public Error handleActiveEquipmentRentalException(ActiveEquipmentRentalException aere) {
        return new Error("ERROR", "equipment.has.active.rentals", "Equipment with ID " + aere.getEquipmentId() + " is currently rented.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(RentalStatusNotFoundException.class)
    public Error handleEquipmentAvailableException(RentalStatusNotFoundException rsnfe) {
        return new Error("ERROR", "rental.status.does.not.exist", "Rental status " + rsnfe.getStatus() + " not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(RentalNotFoundException.class)
    public Error handleEquipmentAvailableException(RentalNotFoundException rnfe) {
        return new Error("ERROR", "rental.does.not.exist", "Rental with ID " + rnfe.getRentalId() + " not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(InvalidRentalStatusChangeException.class)
    public Error handleInvalidRentalStatusException(InvalidRentalStatusChangeException irsce) {
        return new Error("ERROR", "rental.status.not.available", "Rental status change from " + irsce.getNowRentalStatus() + " to " + irsce.getRentalStatusToBeChangedFor() + " is invalid.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(TierNotAvailableException.class)
    public Error handleTierNotAvailableException(TierNotAvailableException tnae) {
        return new Error("ERROR", "rental.tier.not.available", "Rental tier with ID " + tnae.getRentalTierId() + " not available.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(WeatherForecastNotAvailableException.class)
    public Error handleWeatherForecastNotAvailableException(WeatherForecastNotAvailableException wfnae) {
        return new Error("ERROR", "weather.forecast.not.available", "Weather forecast currently not available.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(RenterNotFoundException.class)
    public Error handleRenterNotFoundException(RenterNotFoundException rnfe) {
        return new Error("ERROR", "renter.does.not.exist", "Renter with ID " + rnfe.getRenterId() + " not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(RenterAlreadyExistsException.class)
    public Error handleRenterAlreadyExistsException(RenterAlreadyExistsException raee) {
        return new Error("ERROR", "renter.already.exists", "Renter with email " + raee.getEmail() + " already exists.");
    }
}
