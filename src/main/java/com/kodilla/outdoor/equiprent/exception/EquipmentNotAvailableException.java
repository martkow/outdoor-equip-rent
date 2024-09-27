package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class EquipmentNotAvailableException extends Exception {
    private Long equipmentId;

    public EquipmentNotAvailableException(Long equipmentId) {
        super("Equipment with ID " + equipmentId + " not available.");
        this.equipmentId = equipmentId;
    }
}
