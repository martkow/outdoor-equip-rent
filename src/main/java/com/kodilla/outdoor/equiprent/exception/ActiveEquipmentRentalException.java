package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class ActiveEquipmentRentalException extends Exception {
    private Long equipmentId;

    public ActiveEquipmentRentalException(Long equipmentId) {
        super("Equipment with ID " + equipmentId + " is currently rented.");
        this.equipmentId = equipmentId;
    }
}
