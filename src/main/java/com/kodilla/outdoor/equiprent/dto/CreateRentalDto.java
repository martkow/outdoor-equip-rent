package com.kodilla.outdoor.equiprent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateRentalDto {
    @Schema(description = "ID of the equipment being rented", example = "1", required = true)
    private Long equipmentId;
    @Schema(description = "ID of the person renting the equipment", example = "1", required = true)
    private Long renterId;
    @Schema(description = "Rental tier id for the equipment", example = "1", required = true)
    private Long rentalTierId;
    @Schema(description = "Number of rental tier units", example = "3", required = true)
    private Integer rentalTierQuantity;
}
