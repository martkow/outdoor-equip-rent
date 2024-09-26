package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.domain.EquipmentPrice;
import com.kodilla.outdoor.equiprent.domain.Tier;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for EquipmentPriceRepository")
@SpringBootTest
@Transactional
public class EquipmentPriceRepositoryTests {
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EquipmentPriceRepository equipmentPriceRepository;

    @DisplayName("Test case for finding EquipmentPrice by Id")
    @Test
    void shouldFindEquipmentPriceById() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(null, equipment, Tier.DAY, new BigDecimal("100.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        equipment.setPrices(List.of(equipmentPrice));

        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentPriceId = savedEquipment.getPrices().get(0).getId();
        // When
        Optional<EquipmentPrice> result = equipmentPriceRepository.findById(savedEquipmentPriceId);
        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(savedEquipmentPriceId, result.get().getId());
        Assertions.assertEquals(Tier.DAY, result.get().getTier());
        Assertions.assertEquals(new BigDecimal("100.00"), result.get().getPrice());
        Assertions.assertEquals("Tent Plus", result.get().getEquipment().getName());
    }

    @DisplayName("Test case for finding EquipmentPrice by Equipment and Tier")
    @Test
    void shouldFindEquipmentPriceByEquipmentAndTier() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(null, equipment, Tier.DAY, new BigDecimal("100.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        equipment.setPrices(List.of(equipmentPrice));
        Equipment savedEquipment = equipmentRepository.save(equipment);
        // When
        Optional<EquipmentPrice> result = equipmentPriceRepository.findByEquipmentAndTier(savedEquipment, Tier.DAY);
        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(Tier.DAY, result.get().getTier());
        Assertions.assertEquals(new BigDecimal("100.00"), result.get().getPrice());
        Assertions.assertEquals("Tent Plus", result.get().getEquipment().getName());
    }
}
