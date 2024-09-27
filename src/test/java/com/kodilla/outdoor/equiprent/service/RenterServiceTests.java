package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.exception.RenterAlreadyExistsException;
import com.kodilla.outdoor.equiprent.exception.RenterNotFoundException;
import com.kodilla.outdoor.equiprent.domain.Renter;
import com.kodilla.outdoor.equiprent.repository.RenterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for RenterService")
@ExtendWith(MockitoExtension.class)
public class RenterServiceTests {
    @InjectMocks
    private RenterService renterService;
    @Mock
    private RenterRepository renterRepository;
    private Renter renter;

    @BeforeEach
    void setUp() {
        renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
    }

    @Test
    @DisplayName("Test for creating a renter when renter does not already exist")
    void shouldCreateRenter() throws RenterAlreadyExistsException {
        // Given
        Mockito.when(renterRepository.existsByEmail(renter.getEmail())).thenReturn(false);
        Mockito.when(renterRepository.save(Mockito.any(Renter.class))).thenReturn(renter);
        // When
        Renter result = renterService.createRenter(renter);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(renter.getEmail(), result.getEmail());
        Mockito.verify(renterRepository, Mockito.times(1)).save(renter);
    }

    @Test
    @DisplayName("Test for creating a renter when renter already exists")
    void shouldThrowRenterAlreadyExistsException() {
        // Given
        Mockito.when(renterRepository.existsByEmail(renter.getEmail())).thenReturn(true);
        // When & Then
        Assertions.assertThrows(RenterAlreadyExistsException.class, () -> renterService.createRenter(renter));
        Mockito.verify(renterRepository, Mockito.times(0)).save(Mockito.any(Renter.class));
    }

    @Test
    @DisplayName("Test for getting all renters")
    void shouldReturnAllRenters() {
        // Given
        List<Renter> renters = Arrays.asList(renter);
        Mockito.when(renterRepository.findAll()).thenReturn(renters);
        // When
        List<Renter> result = renterService.getAllRenters();
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(renter, result.get(0));
        Mockito.verify(renterRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Test for retrieving a renter by ID")
    void shouldReturnRenterById() throws RenterNotFoundException {
        // Given
        Mockito.when(renterRepository.findById(renter.getId())).thenReturn(Optional.of(renter));
        // When
        Renter result = renterService.getRenter(renter.getId());
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(renter, result);
        Mockito.verify(renterRepository, Mockito.times(1)).findById(renter.getId());
    }

    @Test
    @DisplayName("Test for RenterNotFoundException when renter is not found by ID")
    void shouldThrowRenterNotFoundException() {
        // Given
        Mockito.when(renterRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // When & Then
        Assertions.assertThrows(RenterNotFoundException.class, () -> renterService.getRenter(1L));
        Mockito.verify(renterRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test for updating a renter")
    void shouldUpdateRenter() throws RenterNotFoundException {
        // Given
        Renter updatedRenter = new Renter(1L, "Bubuslawu", "Bubuslawskiu", "bubuslawu@test.pl", "000000001", "Bubuslawskau 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), LocalDateTime.of(2024, 9, 24, 15, 0, 0));

        Mockito.when(renterRepository.findById(renter.getId())).thenReturn(Optional.of(renter));
        Mockito.when(renterRepository.save(Mockito.any(Renter.class))).thenReturn(updatedRenter);
        // When
        renterService.updateRenter(renter.getId(), renter);
        // Then
        Assertions.assertEquals(updatedRenter.getFirstName(), "Bubuslawu");
        Assertions.assertEquals(updatedRenter.getLastName(), "Bubuslawskiu");
        Assertions.assertEquals(updatedRenter.getEmail(), "bubuslawu@test.pl");
        Assertions.assertEquals(updatedRenter.getPhoneNumber(), "000000001");
        Assertions.assertEquals(updatedRenter.getAddress(), "Bubuslawskau 1");
        Assertions.assertEquals(updatedRenter.getCreationDate(), LocalDateTime.of(2024, 9, 24, 14, 0, 0));
        Assertions.assertEquals(updatedRenter.getModificationDate(), LocalDateTime.of(2024, 9, 24, 15, 0, 0));
        Mockito.verify(renterRepository, Mockito.times(1)).save(renter);
    }

    @Test
    @DisplayName("Test for RenterNotFoundException when updating a non-existing renter")
    void shouldThrowRenterNotFoundExceptionWhenUpdatingNonExistingRenter() {
        // Given
        Mockito.when(renterRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // When & Then
        Assertions.assertThrows(RenterNotFoundException.class, () -> renterService.updateRenter(1L, renter));
        Mockito.verify(renterRepository, Mockito.times(1)).findById(1L);
    }
}
