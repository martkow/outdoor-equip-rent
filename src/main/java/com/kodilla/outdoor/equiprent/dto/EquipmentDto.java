package com.kodilla.outdoor.equiprent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class EquipmentDto {
    @Schema(description = "Unique identifier for the equipment", example = "1")
    private Long id;
    @Schema(description = "Name of the equipment", example = "Tent plus")
    private String name;
    @Schema(description = "Description of the equipment", example = "A spacious tent for 4 people")
    private String description;
    @Schema(description = "Category of the equipment", example = "TENT")
    private String category;
    @Schema(description = "Current quantity of the equipment available", example = "8")
    @JsonProperty("availableQuantity")
    private Long currentQuantity;
    @Schema(description = "List of prices for the equipment at different time intervals")
    private List<EquipmentPriceDto> prices;
}
