package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.EquipmentAvailability;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@DisplayName("Tests for EquipmentAvailabilityRepository")
@SpringBootTest
@Transactional
public class EquipmentAvailabilityRepositoryTests {
    @Autowired
    private EquipmentAvailabilityRepository equipmentAvailabilityRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EntityManager entityManager;

    @DisplayName("Test case for finding EquipmentAvailability by Id")
    @Test
    void shouldFindEquipmentAvailabilityById() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(null, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        equipment.setEquipmentAvailability(equipmentAvailability);

        Equipment savedEquipment = equipmentRepository.save(equipment);

        Long savedEquipmentAvailabilityId = savedEquipment.getEquipmentAvailability().getId();
        // When
        Optional<EquipmentAvailability> result = equipmentAvailabilityRepository.findById(savedEquipmentAvailabilityId);
        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(savedEquipmentAvailabilityId, result.get().getEquipment().getEquipmentAvailability().getId());
    }

//    @DisplayName("Test case that EquipmentAvailability removal does not remove Equipment and sets equipment's availability to null")
//    @Test
//    void shouldNotRemoveEquipmentAndSetNullToEquipmentAvailabilityFieldWhenEquipmentAvailabilityIsDeleted() {
//        // Given
//        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
//        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(null, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
//
//        equipment.setEquipmentAvailability(equipmentAvailability);
//
//        Equipment savedEquipment = equipmentRepository.save(equipment);
//        Long savedEquipmentId = savedEquipment.getId();
//        // When
//        equipmentAvailabilityRepository.deleteById(equipmentAvailability.getId());
//
//        entityManager.clear();
//
//        Optional<EquipmentAvailability> deletedEquipmentAvailability = equipmentAvailabilityRepository.findById(equipmentAvailability.getId());
//
//        Optional<Equipment> result = equipmentRepository.findById(savedEquipmentId);
//
//        // Then
//        Assertions.assertFalse(deletedEquipmentAvailability.isPresent());
//        Assertions.assertTrue(result.isPresent());
//        Assertions.assertNull(result.get().getEquipmentAvailability());
//    }
}
