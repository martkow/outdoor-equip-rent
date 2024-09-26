package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.*;
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

@DisplayName("Tests for RentalRepository")
@SpringBootTest
@Transactional
public class RentalRepositoryTests {
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private RenterRepository renterRepository;

    @DisplayName("Test case for checking save method")
    @Test
    void shouldSaveAndReturnRental() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        equipmentRepository.save(equipment);

        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        renterRepository.save(renter);

        Rental rental = new Rental(null, equipment, renter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());
        // When
        Rental savedRental = rentalRepository.save(rental);
        // Then
        Assertions.assertNotNull(savedRental.getId());
    }


    @DisplayName("Test case for checking if rental exists by equipment and status")
    @Test
    void shouldCheckThatRentalExistsByEquipmentAndStatus() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(null, equipment, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(null, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));

        equipment.setEquipmentAvailability(equipmentAvailability);
        equipment.setPrices(List.of(equipmentPrice));

        Equipment savedEquipment = equipmentRepository.save(equipment);

        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0));
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, savedEquipment, savedRenter, LocalDateTime.of(2024, 9, 24, 12, 0, 0), LocalDateTime.of(2024, 9, 24, 14, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        rentalRepository.save(rental);
        // When
        boolean result = rentalRepository.existsRentalByEquipmentAndStatus(equipment, RentalStatus.ACTIVE);
        // Then
        Assertions.assertTrue(result);
    }

    @DisplayName("Test case for checking findAll method")
    @Test
    void shouldReturnAllRentals() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        equipmentRepository.save(equipment);

        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, equipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());
        rentalRepository.save(rental);
        // When
        List<Rental> rentals = rentalRepository.findAll();
        // Then
        Assertions.assertEquals(1, rentals.size());
        Assertions.assertTrue(rentals.contains(rental));
    }

    @DisplayName("Test case for checking findByStatusIn method")
    @Test
    void shouldReturnRentalsByStatuses() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        equipmentRepository.save(equipment);

        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        Renter savedRenter = renterRepository.save(renter);

        Rental rentalActive = new Rental(null, equipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());
        Rental rentalCompleted = new Rental(null, equipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(3), null, RentalStatus.COMPLETED, new BigDecimal("15.00"), LocalDateTime.now());

        rentalRepository.save(rentalActive);
        rentalRepository.save(rentalCompleted);
        // When
        List<RentalStatus> statuses = List.of(RentalStatus.ACTIVE);
        List<Rental> rentals = rentalRepository.findByStatusIn(statuses);
        // Then
        Assertions.assertEquals(1, rentals.size());
        Assertions.assertTrue(rentals.contains(rentalActive));
        Assertions.assertFalse(rentals.contains(rentalCompleted));
    }

    @DisplayName("Test case for checking findById method")
    @Test
    void shouldReturnRentalById() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        equipmentRepository.save(equipment);

        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, equipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());
        Rental savedRental = rentalRepository.save(rental);
        // When
        Optional<Rental> foundRental = rentalRepository.findById(savedRental.getId());
        // Then
        Assertions.assertTrue(foundRental.isPresent());
        Assertions.assertEquals(savedRental.getId(), foundRental.get().getId());
    }

    @DisplayName("Test case for checking findByRenterId method")
    @Test
    void shouldReturnRentalsByRenterId() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        equipmentRepository.save(equipment);

        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, equipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());

        rentalRepository.save(rental);
        // When
        List<Rental> rentals = rentalRepository.findByRenterId(savedRenter.getId());
        // Then
        Assertions.assertEquals(1, rentals.size());
        Assertions.assertTrue(rentals.contains(rental));
    }

    @DisplayName("Test case for checking that Equipment and Renter are not deleted when Rental is removed")
    @Test
    void shouldNotDeleteEquipmentAndRenterWhenRentalIsRemoved() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        Equipment savedEquipment = equipmentRepository.save(equipment);

        Renter renter = new Renter(null, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, savedEquipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());
        Rental savedRental = rentalRepository.save(rental);
        Long savedRentalId = savedRental.getId();
        // When
        rentalRepository.deleteById(savedRentalId);
        // Then
        Assertions.assertTrue(equipmentRepository.findById(savedRentalId).isPresent());
        Assertions.assertTrue(renterRepository.findById(savedRentalId).isPresent());
    }

    @DisplayName("Test case for deleting Rental by ID")
    @Test
    void shouldDeleteRentalById() {
        // Given
        Equipment equipment = new Equipment(null, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        Equipment savedEquipment = equipmentRepository.save(equipment);

        Renter renter = new Renter(null, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.now());
        Renter savedRenter = renterRepository.save(renter);

        Rental rental = new Rental(null, savedEquipment, savedRenter, LocalDateTime.now(), LocalDateTime.now().plusHours(2), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), LocalDateTime.now());
        Rental savedRental = rentalRepository.save(rental);
        Long savedRentalId = savedRental.getId();
        // When
        rentalRepository.deleteById(savedRentalId);
        // Then
        Assertions.assertFalse(rentalRepository.findById(savedRentalId).isPresent());
    }

}
