package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for EquipmentRepository")
@SpringBootTest
@Transactional
public class EquipmentRepositoryTests {
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EquipmentAvailabilityRepository equipmentAvailabilityRepository;
    @Autowired
    private EquipmentPriceRepository equipmentPriceRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private RenterRepository renterRepository;
    @Autowired
    private EntityManager entityManager;

    @DisplayName("Test case for findAll method")
    @Test
    void shouldFindAllEquipment() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(null, equipment, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(null, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        equipment.setEquipmentAvailability(equipmentAvailability);
        equipment.setPrices(List.of(equipmentPrice));

        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentId = savedEquipment.getId();
        // When
        List<Equipment> result = equipmentRepository.findAll();
        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(savedEquipmentId, result.get(0).getId());
        Assertions.assertEquals("Tent Plus", result.get(0).getName());
        Assertions.assertEquals("Camping tent", result.get(0).getDescription());
        Assertions.assertEquals(EquipmentCategory.TENT, result.get(0).getCategory());
        Assertions.assertEquals(10L, result.get(0).getEquipmentAvailability().getInitialQuantity());
        Assertions.assertEquals(5L, result.get(0).getEquipmentAvailability().getCurrentQuantity());
        Assertions.assertTrue(result.get(0).getEquipmentAvailability().isAvailable());
        Assertions.assertEquals(Tier.HOUR, result.get(0).getPrices().get(0).getTier());
        Assertions.assertEquals(new BigDecimal("1.00"), result.get(0).getPrices().get(0).getPrice());
    }

    @DisplayName("Test case for finding Equipment by categories")
    @Test
    void shouldFindEquipmentByCategoryIn() {
        // Given
        Equipment tent = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        Equipment stove = new Equipment(null, "Camping Stove", "Portable stove", EquipmentCategory.STOVE, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        Equipment backpack = new Equipment(null, "Backpack", "Hiking backpack", EquipmentCategory.BACKPACK, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        EquipmentPrice tentPrice = new EquipmentPrice(null, tent, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability tentAvailability = new EquipmentAvailability(null, tent, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        tent.setEquipmentAvailability(tentAvailability);
        tent.setPrices(List.of(tentPrice));

        EquipmentPrice stovePrice = new EquipmentPrice(null, stove, Tier.DAY, new BigDecimal("5.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability stoveAvailability = new EquipmentAvailability(null, stove, 8L, 4L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        stove.setEquipmentAvailability(stoveAvailability);
        stove.setPrices(List.of(stovePrice));

        EquipmentPrice backpackPrice = new EquipmentPrice(null, backpack, Tier.DAY, new BigDecimal("3.50"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability backpackAvailability = new EquipmentAvailability(null, backpack, 15L, 12L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        backpack.setEquipmentAvailability(backpackAvailability);
        backpack.setPrices(List.of(backpackPrice));

        equipmentRepository.save(tent);
        equipmentRepository.save(stove);
        equipmentRepository.save(backpack);
        // When
        List<EquipmentCategory> categories = List.of(EquipmentCategory.TENT, EquipmentCategory.STOVE);
        List<Equipment> result = equipmentRepository.findByCategoryIn(categories);
        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(e -> e.getCategory() == EquipmentCategory.TENT));
        Assertions.assertTrue(result.stream().anyMatch(e -> e.getCategory() == EquipmentCategory.STOVE));
    }

    @DisplayName("Test case for finding Equipment by ID")
    @Test
    void shouldFindEquipmentById() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentId = savedEquipment.getId();
        // When
        Optional<Equipment> result = equipmentRepository.findById(savedEquipmentId);
        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(savedEquipmentId, result.get().getId());
        Assertions.assertEquals("Tent Plus", result.get().getName());
        Assertions.assertEquals("Camping tent", result.get().getDescription());
        Assertions.assertEquals(EquipmentCategory.TENT, result.get().getCategory());
    }

    @DisplayName("Test case for saving Equipment")
    @Test
    void shouldSaveEquipment() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        // When
        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentId = savedEquipment.getId();
        // Then
        Assertions.assertNotNull(savedEquipmentId);
        Optional<Equipment> result = equipmentRepository.findById(savedEquipmentId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Tent Plus", result.get().getName());
        Assertions.assertEquals("Camping tent", result.get().getDescription());
    }

    @DisplayName("Test case for deleting Equipment by ID")
    @Test
    void shouldDeleteEquipmentById() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentId = savedEquipment.getId();
        // When
        equipmentRepository.deleteById(savedEquipmentId);
        // Then
        Optional<Equipment> result = equipmentRepository.findById(savedEquipmentId);
        Assertions.assertFalse(result.isPresent());
    }

    @DisplayName("Test case for cascading delete for Equipment and related EquipmentAvailability and EquipmentPrice")
    @Test
    void shouldCascadeDeleteEquipmentAvailabilityAndPrices() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(null, equipment, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(null, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        equipment.setEquipmentAvailability(equipmentAvailability);
        equipment.setPrices(List.of(equipmentPrice));

        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentId = savedEquipment.getId();

        Optional<EquipmentAvailability> savedEquipmentAvailability = equipmentAvailabilityRepository.findById(equipmentAvailability.getId());
        Optional<EquipmentPrice> savedEquipmentPrice = equipmentPriceRepository.findById(equipmentPrice.getId());

        Assertions.assertTrue(savedEquipmentAvailability.isPresent());
        Assertions.assertTrue(savedEquipmentPrice.isPresent());
        // When
        equipmentRepository.deleteById(savedEquipmentId);
        // Then
        Optional<EquipmentAvailability> deletedEquipmentAvailability = equipmentAvailabilityRepository.findById(equipmentAvailability.getId());
        Optional<EquipmentPrice> deletedEquipmentPrice = equipmentPriceRepository.findById(equipmentPrice.getId());

        Assertions.assertFalse(deletedEquipmentAvailability.isPresent());
        Assertions.assertFalse(deletedEquipmentPrice.isPresent());
    }

    @DisplayName("Should return validation constraint violation when deleting Equipment with Rentals")
    @Test
    void shouldReturnValidationConstraintViolationWhenDeletingEquipmentWithRentals() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        Equipment savedEquipment = equipmentRepository.save(equipment);
        Long savedEquipmentId = savedEquipment.getId();

        Renter renter = new Renter(null, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, savedEquipment, savedRenter, LocalDateTime.of(2024, 9, 24, 13, 0, 0), LocalDateTime.of(2024, 9, 24, 14, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("11.11"), CurrencyCode.PLN, LocalDateTime.of(2024, 9, 24, 13, 0, 0), null);
        rentalRepository.save(rental);
        // When & Then
        Assertions.assertThrows(org.hibernate.exception.ConstraintViolationException.class, () -> {equipmentRepository.deleteById(savedEquipmentId); entityManager.flush();});
    }
}
