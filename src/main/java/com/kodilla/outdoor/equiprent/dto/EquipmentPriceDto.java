package com.kodilla.outdoor.equiprent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class EquipmentPriceDto {
    @Schema(description = "Unique identifier for the price Tier", example = "1")
    private Long id;
    @Schema(description = "Price tier for the equipment (e.g., HOUR, DAY, WEEK)", example = "DAY")
    private String priceTier;
    @Schema(description = "Price for the specified tier", example = "20.99")
    private BigDecimal price;
}
