package com.kodilla.outdoor.equiprent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class RentalDto {
    @Schema(description = "Unique identifier of the rental", example = "1", required = true)
    private Long id;
    @Schema(description = "ID of the equipment being rented", example = "1", required = true)
    private Long equipmentId;
    @Schema(description = "ID of the person renting the equipment", example = "1", required = true)
    private Long renterId;
    @Schema(description = "Rental start date and time", example = "2024-09-21T10:00:00", required = true)
    private String rentalStart;
    @Schema(description = "Rental end date and time", example = "2024-09-21T12:00:00", required = true)
    private String rentalEnd;
    @Schema(description = "Status of the rental", example = "ACTIVE", required = true)
    private String status;
    @Schema(description = "Total price of the rental", example = "200.11", required = true)
    private BigDecimal totalPrice;
}
