package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.exception.ActiveEquipmentRentalException;
import com.kodilla.outdoor.equiprent.exception.EquipmentNotFoundException;
import com.kodilla.outdoor.equiprent.repository.EquipmentRepository;
import com.kodilla.outdoor.equiprent.repository.RentalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DisplayName("Tests for EquipmentService class")
@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTests {
    @InjectMocks
    private EquipmentService equipmentService;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private RentalRepository rentalRepository;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, new ArrayList<>(), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(1L, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipment.setEquipmentAvailability(equipmentAvailability);
        EquipmentPrice equipmentPrice = new EquipmentPrice(1L, equipment, Tier.HOUR, new BigDecimal("10.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipment.getPrices().add(equipmentPrice);
    }

    @Test
    void shouldReturnAllEquipment() {
        // Given
        Mockito.when(equipmentRepository.findAll()).thenReturn(Arrays.asList(equipment));
        // When
        List<Equipment> result = equipmentService.getAllEquipment();
        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(equipment, result.get(0));
    }

    @Test
    void shouldReturnAllEquipmentWhenCategoriesIsEmpty() {
        // Given
        Mockito.when(equipmentService.getAllEquipment()).thenReturn(Arrays.asList(equipment));
        // When
        List<Equipment> result = equipmentService.getEquipmentByCategories(List.of());
        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(equipment, result.get(0));
    }

    @Test
    void shouldReturnEquipmentByCategoriesWhenCategoriesIsNotEmpty() {
        // Given
        Mockito.when(equipmentRepository.findByCategoryIn(List.of(EquipmentCategory.TENT))).thenReturn(Arrays.asList(equipment));
        // When
        List<Equipment> result = equipmentService.getEquipmentByCategories(List.of(EquipmentCategory.TENT));
        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(equipment, result.get(0));
    }

    @Test
    @DisplayName("Test for retrieving all equipment categories")
    void shouldReturnAllCategories() {
        // Given
        EquipmentCategory[] expectedCategories = EquipmentCategory.values();
        // When
        List<EquipmentCategory> result = equipmentService.getAllCategories();
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedCategories.length, result.size());
        Assertions.assertArrayEquals(expectedCategories, result.toArray());
    }

    @Test
    @DisplayName("Test case for adding equipment")
    void shouldAddEquipment() {
        // Given
        Mockito.when(equipmentRepository.save(Mockito.any())).thenReturn(equipment);
        // When
        Equipment result = equipmentService.addEquipment(equipment);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Tent Plus", result.getName());
        Assertions.assertEquals("Camping tent", result.getDescription());
        Assertions.assertTrue(result.getEquipmentAvailability().isAvailable());
        Assertions.assertNotNull(result.getPrices());
        Assertions.assertEquals(1, result.getPrices().size());
        Assertions.assertNotNull(result.getPrices().get(0).getCreationDate());
    }

    @Test
    @DisplayName("Test for updating equipment availability when equipment exists and has no active rentals")
    void shouldUpdateEquipmentAvailability() throws ActiveEquipmentRentalException, EquipmentNotFoundException {
        // Given
        equipment.getEquipmentAvailability().setAvailable(true);
        Mockito.when(equipmentRepository.findById(equipment.getId())).thenReturn(java.util.Optional.of(equipment));
        Mockito.when(rentalRepository.existsRentalByEquipmentAndStatus(equipment, RentalStatus.ACTIVE)).thenReturn(false);
        // When
        equipmentService.updateEquipmentAvailability(equipment.getId());
        // Then
        Assertions.assertFalse(equipment.getEquipmentAvailability().isAvailable());
        Mockito.verify(equipmentRepository).save(equipment);
    }

    @Test
    @DisplayName("Test for EquipmentNotFoundException when updating availability of non-existing equipment")
    void shouldThrowEquipmentNotFoundExceptionWhenEquipmentDoesNotExist() {
        // Given
        Mockito.when(equipmentRepository.findById(Mockito.any())).thenReturn(java.util.Optional.empty());
        // When & Then
        Assertions.assertThrows(EquipmentNotFoundException.class, () -> equipmentService.updateEquipmentAvailability(1L));
    }

    @Test
    @DisplayName("Test for ActiveEquipmentRentalException when equipment has active rentals")
    void shouldThrowActiveEquipmentRentalExceptionWhenEquipmentIsRented() {
        // Given
        Mockito.when(equipmentRepository.findById(equipment.getId())).thenReturn(java.util.Optional.of(equipment));
        Mockito.when(rentalRepository.existsRentalByEquipmentAndStatus(equipment, RentalStatus.ACTIVE)).thenReturn(true);
        // When & Then
        Assertions.assertThrows(ActiveEquipmentRentalException.class, () -> equipmentService.updateEquipmentAvailability(equipment.getId()));
    }
}