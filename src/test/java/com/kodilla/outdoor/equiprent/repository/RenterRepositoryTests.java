package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for RenterRepository")
@SpringBootTest
@Transactional
public class RenterRepositoryTests {
    @Autowired
    private RenterRepository renterRepository;

    @DisplayName("Test case for finding renter by ID")
    @Test
    void shouldFindRenterById() {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        Renter savedRenter = renterRepository.save(renter);
        Long savedRenterId = savedRenter.getId();
        // When
        Optional<Renter> result = renterRepository.findById(savedRenterId);
        // Then
        Assertions.assertTrue(result.isPresent(), "Renter should be found");
        Renter foundRenter = result.get();
        Assertions.assertEquals("Bubuslaw", foundRenter.getFirstName());
        Assertions.assertEquals("Bubuslawski", foundRenter.getLastName());
        Assertions.assertEquals("bubuslaw@test.pl", foundRenter.getEmail());
        Assertions.assertEquals("000000000", foundRenter.getPhoneNumber());
        Assertions.assertEquals("Bubuslawska 1", foundRenter.getAddress());
        Assertions.assertEquals(LocalDateTime.of(2024, 9, 24, 14, 0, 0), foundRenter.getCreationDate());
    }

    @DisplayName("Test case for checking if renter exists by email")
    @Test
    void shouldCheckThatRenterExistsByEmail() {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        renterRepository.save(renter);
        // When
        boolean result = renterRepository.existsByEmail("bubuslaw@test.pl");
        // Then
        Assertions.assertTrue(result);
    }

    @DisplayName("Test case for saving renter")
    @Test
    void shouldSaveRenter() {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        // When
        Renter savedRenter = renterRepository.save(renter);
        // Then
        Assertions.assertNotNull(savedRenter.getId());
        Assertions.assertEquals("Bubuslaw", savedRenter.getFirstName());
        Assertions.assertEquals("Bubuslawski", savedRenter.getLastName());
        Assertions.assertEquals("bubuslaw@test.pl", savedRenter.getEmail());
        Assertions.assertEquals("000000000", savedRenter.getPhoneNumber());
        Assertions.assertEquals("Bubuslawska 1", savedRenter.getAddress());
        Assertions.assertEquals(LocalDateTime.of(2024, 9, 24, 14, 0, 0), savedRenter.getCreationDate());
    }

    @DisplayName("Test case for finding all renters")
    @Test
    void shouldFindAllRenters() {
        // Given
        Renter renter1 = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        Renter renter2 = new Renter(1L, "Bubuslaw2", "Bubuslawski2", "bubuslaw2@test.pl", "000000002", "Bubuslawska 2", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        renterRepository.save(renter1);
        renterRepository.save(renter2);
        // When
        List<Renter> renters = renterRepository.findAll();
        // Then
        Assertions.assertEquals(2, renters.size());
        Assertions.assertTrue(renters.stream().anyMatch(r -> r.getEmail().equals("bubuslaw@test.pl")));
        Assertions.assertTrue(renters.stream().anyMatch(r -> r.getEmail().equals("bubuslaw2@test.pl")));
    }
}
