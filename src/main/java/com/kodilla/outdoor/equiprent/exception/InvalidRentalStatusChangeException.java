package com.kodilla.outdoor.equiprent.exception;

import com.kodilla.outdoor.equiprent.domain.RentalStatus;
import lombok.Getter;

@Getter
public class InvalidRentalStatusChangeException extends Exception {
    private RentalStatus rentalStatusToBeChangedFor;
    private RentalStatus nowRentalStatus;

    public InvalidRentalStatusChangeException(RentalStatus rentalStatusToBeChangedFor, RentalStatus nowRentalStatus) {
        super("Rental status change from " + nowRentalStatus + " to " + rentalStatusToBeChangedFor + " is invalid");
        this.rentalStatusToBeChangedFor = rentalStatusToBeChangedFor;
        this.nowRentalStatus = nowRentalStatus;
    }
}
