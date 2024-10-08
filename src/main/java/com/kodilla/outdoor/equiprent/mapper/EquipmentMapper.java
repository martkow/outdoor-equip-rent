package com.kodilla.outdoor.equiprent.mapper;

import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentDto;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentPriceDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentPriceDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EquipmentMapper {
    public List<EquipmentDto> mapEquipmentListToEquipmentDtoList(List<Equipment> equipmentList) {
        return equipmentList.stream()
                .map(this::mapEquipmentToEquipmentDto)
                .toList();
    }

    public EquipmentDto mapEquipmentToEquipmentDto(Equipment equipment) {
        return EquipmentDto.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .description(equipment.getDescription())
                .category(equipment.getCategory().toString())
                .currentQuantity(equipment.getEquipmentAvailability().getCurrentQuantity())
                .prices(equipment.getPrices().stream()
                        .map(this::mapEquipmentPriceToEquipmentPriceDto)
                        .toList())
                .build();
    }

    public EquipmentPriceDto mapEquipmentPriceToEquipmentPriceDto(EquipmentPrice equipmentPrice) {
        return EquipmentPriceDto.builder()
                .id(equipmentPrice.getId())
                .priceTier(equipmentPrice.getTier().toString())
                .price(equipmentPrice.getPrice())
                .build();
    }

    public List<EquipmentPrice> mapCreateEquipmentPriceDtoListToEquipmentPriceList(Equipment equipment, List<CreateEquipmentPriceDto> createEquipmentPriceDtoList) {
        return createEquipmentPriceDtoList.stream()
                .map(cepdto -> mapCreateEquipmentPriceDtoToEquipmentPrice(equipment, cepdto))
                .toList();
    }

    public EquipmentPrice mapCreateEquipmentPriceDtoToEquipmentPrice(Equipment equipment, CreateEquipmentPriceDto createEquipmentPriceDto) {
        return EquipmentPrice.builder()
                .equipment(equipment)
                .tier(createEquipmentPriceDto.getPriceTier())
                .price(createEquipmentPriceDto.getPrice())
                .build();
    }

    public EquipmentAvailability mapCurrentQuantityToEquipmentAvailability(Equipment equipment, Long currentQuantity) {
        return EquipmentAvailability.builder()
                .equipment(equipment)
                .initialQuantity(currentQuantity)
                .currentQuantity(currentQuantity)
                .build();
    }

    public Equipment mapCreateEquipmentDtoToEquipment(EquipmentCategory equipmentCategory, CreateEquipmentDto createEquipmentDto) {
        Equipment equipment = Equipment.builder()
                .name(createEquipmentDto.getName())
                .description(createEquipmentDto.getDescription())
                .category(equipmentCategory)
                .prices(new ArrayList<>())
                .build();

        equipment.setPrices(mapCreateEquipmentPriceDtoListToEquipmentPriceList(equipment, createEquipmentDto.getPrices()));

        equipment.setEquipmentAvailability(mapCurrentQuantityToEquipmentAvailability(equipment, createEquipmentDto.getCurrentQuantity()));

        return equipment;
    }
}
