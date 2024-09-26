package com.kodilla.outdoor.equiprent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CreateEquipmentDto {
    @Schema(description = "Name of the equipment", example = "Tent plus")
    private String name;
    @Schema(description = "Description of the equipment", example = "A spacious tent for 4 people")
    private String description;
    @Schema(description = "Current quantity of the equipment available", example = "8")
    @JsonProperty("availableQuantity")
    private Long currentQuantity;
    @Schema(description = "List of prices for the equipment at different time intervals")
    private List<CreateEquipmentPriceDto> prices;
}
