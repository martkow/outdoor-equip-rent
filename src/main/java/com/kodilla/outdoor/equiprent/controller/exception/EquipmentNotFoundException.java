package com.kodilla.outdoor.equiprent.controller.exception;

import lombok.Getter;

@Getter
public class EquipmentNotFoundException extends Exception {
    private Long equipmentId;

    public EquipmentNotFoundException(Long equipmentId) {
        super("Equipment with ID " + equipmentId + " does not exist.");
        this.equipmentId = equipmentId;
    }
}
