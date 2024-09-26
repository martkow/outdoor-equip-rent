package com.kodilla.outdoor.equiprent.dto;

import com.kodilla.outdoor.equiprent.domain.Tier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CreateEquipmentPriceDto {
    @Schema(description = "Price tier for the equipment (e.g., HOUR, DAY, WEEK)", example = "DAY")
    private Tier priceTier;
    @Schema(description = "Price for the specified tier", example = "20.99")
    private BigDecimal price;
}
