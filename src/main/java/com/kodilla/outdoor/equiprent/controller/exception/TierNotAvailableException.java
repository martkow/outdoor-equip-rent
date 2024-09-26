package com.kodilla.outdoor.equiprent.controller.exception;

import com.kodilla.outdoor.equiprent.domain.Tier;
import lombok.Getter;

@Getter
public class TierNotAvailableException extends Exception {
    private Long rentalTierId;

    public TierNotAvailableException(Long rentalTierId) {
        super("Tier with ID " + rentalTierId + " not available.");
        this.rentalTierId = rentalTierId;
    }
}
