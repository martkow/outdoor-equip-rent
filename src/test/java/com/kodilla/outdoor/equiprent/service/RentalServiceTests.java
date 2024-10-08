package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.dto.CreateRentalDto;
import com.kodilla.outdoor.equiprent.dto.ExchangeRateDto;
import com.kodilla.outdoor.equiprent.exception.*;
import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.external.api.nbp.pl.client.ApiNbpPlClient;
import com.kodilla.outdoor.equiprent.repository.EquipmentPriceRepository;
import com.kodilla.outdoor.equiprent.repository.EquipmentRepository;
import com.kodilla.outdoor.equiprent.repository.RentalRepository;
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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for RentalService")
@ExtendWith(MockitoExtension.class)
public class RentalServiceTests {
    @InjectMocks
    private RentalService rentalService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private EquipmentPriceRepository equipmentPriceRepository;
    @Mock
    private RenterRepository renterRepository;
    @Mock
    private ApiNbpPlClient apiNbpPlClient;
    private Equipment equipment;
    private EquipmentAvailability equipmentAvailability;
    private EquipmentPrice equipmentPrice;
    private Renter renter;
    private CreateRentalDto createRentalDto;
    private Rental rental;
    private Rental completedRental;
    private ExchangeRateDto exchangeRateDto;

    @BeforeEach
    void setUp() {
        equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipmentAvailability = new EquipmentAvailability(1L, equipment, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipmentPrice = new EquipmentPrice(1L, equipment, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipment.setEquipmentAvailability(equipmentAvailability);
        equipment.setPrices(List.of(equipmentPrice));
        renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        createRentalDto = new CreateRentalDto(equipment.getId(), renter.getId(), 1L, 10);
        rental = new Rental(1L, equipment, renter, LocalDateTime.of(2024, 9, 24, 12, 0, 0), LocalDateTime.of(2024, 9, 24, 14, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("10.00"), CurrencyCode.PLN, LocalDateTime.of(2024, 9, 24, 13, 0, 0), null);
        completedRental = new Rental(2L, equipment, renter, LocalDateTime.of(2024, 9, 24, 12, 0, 0), LocalDateTime.of(2024, 9, 24, 14, 0, 0), null, RentalStatus.COMPLETED, new BigDecimal("10.00"), CurrencyCode.PLN, LocalDateTime.of(2024, 9, 24, 13, 0, 0), null);
        exchangeRateDto = new ExchangeRateDto("A", "euro","EUR", List.of(new ExchangeRateDto.Rate("exchangeRateNumber", "effectiveDate", 4.00)));
    }

    @Test
    @DisplayName("Test for creating a rental successfully")
    void shouldCreateRental() throws RenterNotFoundException, TierNotAvailableException, EquipmentNotFoundException, EquipmentNotAvailableException, ExchangeRateNotAvailableException {
        // Given
        Long equipmentId = equipment.getId();
        Long renterId = renter.getId();
        Long rentalTierId = equipmentPrice.getId();

        Mockito.when(equipmentRepository.findById(equipmentId)).thenReturn(java.util.Optional.of(equipment));
        Mockito.when(equipmentPriceRepository.findById(rentalTierId)).thenReturn(java.util.Optional.of(equipmentPrice));
        Mockito.when(renterRepository.findById(renterId)).thenReturn(java.util.Optional.of(renter));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // When
        Rental result = rentalService.createRental(createRentalDto, CurrencyCode.PLN);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(equipment, result.getEquipment());
        Assertions.assertEquals(renter, result.getRenter());
        Assertions.assertNotNull(result.getRentalStart());
        Assertions.assertNotNull(result.getRentalEnd());
        Assertions.assertEquals(RentalStatus.ACTIVE, result.getStatus());
        Assertions.assertNotNull(result.getTotalPrice());
        Assertions.assertEquals(CurrencyCode.PLN, result.getCurrencyCode());
        Assertions.assertNotNull(result.getCreationDate());
        Assertions.assertEquals(4L, equipment.getEquipmentAvailability().getCurrentQuantity());
    }

    @Test
    @DisplayName("Test case for EquipmentNotFoundException when creating a rental")
    void shouldThrowEquipmentNotFoundExceptionWhenEquipmentDoesNotExist() {
        // Given
        Long equipmentId = 1L;

        Mockito.when(equipmentRepository.findById(equipmentId)).thenReturn(java.util.Optional.empty());
        // When & Then
        Assertions.assertThrows(EquipmentNotFoundException.class, () -> rentalService.createRental(createRentalDto, CurrencyCode.PLN));
    }

    @Test
    @DisplayName("Test case for EquipmentNotAvailableException for not available equipment when creating a rental")
    void shouldThrowEquipmentNotAvailableExceptionWhenEquipmentIsNotAvailable() {
        // Given
        Long equipmentId = equipment.getId();

        Mockito.when(equipmentRepository.findById(equipmentId)).thenReturn(java.util.Optional.of(equipment));
        equipment.getEquipmentAvailability().setAvailable(false);
        // When & Then
        Assertions.assertThrows(EquipmentNotAvailableException.class, () -> rentalService.createRental(createRentalDto, CurrencyCode.PLN));
    }

    @Test
    @DisplayName("Test case for TierNotAvailableException for non-existing rental tier when creating a rental")
    void shouldThrowTierNotAvailableExceptionWhenRentalTierDoesNotExist() {
        // Given
        Long equipmentId = equipment.getId();
        Long rentalTierId = 1L;

        Mockito.when(equipmentRepository.findById(equipmentId)).thenReturn(java.util.Optional.of(equipment));
        Mockito.when(equipmentPriceRepository.findById(rentalTierId)).thenReturn(java.util.Optional.empty());
        // When & Then
        Assertions.assertThrows(TierNotAvailableException.class, () -> rentalService.createRental(createRentalDto, CurrencyCode.PLN));
    }

    @Test
    @DisplayName("Test case for RenterNotFoundException for non-existing renter when creating a rental")
    void shouldThrowRenterNotFoundExceptionWhenRenterDoesNotExist() {
        // Given
        Long equipmentId = equipment.getId();
        Long renterId = 1L;
        Long rentalTierId = equipmentPrice.getId();

        Mockito.when(equipmentRepository.findById(equipmentId)).thenReturn(java.util.Optional.of(equipment));
        Mockito.when(equipmentPriceRepository.findById(rentalTierId)).thenReturn(java.util.Optional.of(equipmentPrice));
        Mockito.when(renterRepository.findById(renterId)).thenReturn(java.util.Optional.empty());
        // When & Then
        Assertions.assertThrows(RenterNotFoundException.class, () -> rentalService.createRental(createRentalDto, CurrencyCode.PLN));
    }

    @Test
    @DisplayName("Test for retrieving all rentals")
    void shouldReturnAllRentals() {
        // Given
        Mockito.when(rentalRepository.findAll()).thenReturn(Arrays.asList(rental));
        // When
        List<Rental> result = rentalService.getAllRentals();
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(rental, result.get(0));
    }

    @Test
    @DisplayName("Test for retrieving rentals by statuses when statuses is empty")
    void shouldReturnAllRentalsWhenStatusesIsEmpty() {
        // Given
        Mockito.when(rentalRepository.findAll()).thenReturn(Arrays.asList(rental));
        // When
        List<Rental> result = rentalService.getRentalByStatuses(List.of());
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(rental, result.get(0));
    }

    @Test
    @DisplayName("Test for retrieving rentals by statuses when statuses is not empty")
    void shouldReturnRentalsByStatusesWhenStatusesIsNotEmpty() {
        // Given
        Mockito.when(rentalRepository.findByStatusIn(List.of(RentalStatus.ACTIVE))).thenReturn(Arrays.asList(rental));
        // When
        List<Rental> result = rentalService.getRentalByStatuses(List.of(RentalStatus.ACTIVE));
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(rental, result.get(0));
    }

    @Test
    @DisplayName("Test for updating rental status successfully")
    void shouldUpdateRentalStatus() throws RentalNotFoundException, InvalidRentalStatusChangeException {
        // Given
        Long rentalId = rental.getId();
        RentalStatus newStatus = RentalStatus.COMPLETED;

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(java.util.Optional.of(rental));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // When
        Rental result = rentalService.updateRentalStatus(rentalId, newStatus);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(newStatus, result.getStatus());
        Mockito.verify(rentalRepository).save(rental);
    }

    @Test
    @DisplayName("Test case for RentalNotFoundException when updating rental status")
    void shouldThrowRentalNotFoundExceptionWhenRentalDoesNotExist() {
        // Given
        Long rentalId = 1L;

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(java.util.Optional.empty());
        // When & Then
        Assertions.assertThrows(RentalNotFoundException.class, () -> rentalService.updateRentalStatus(rentalId, RentalStatus.COMPLETED));
    }

    @Test
    @DisplayName("Test case for InvalidRentalStatusChangeException when updating rental status")
    void shouldThrowInvalidRentalStatusChangeExceptionWhenRentalStatusChangeIsInvalid() {
        // Given
        Long rentalId = completedRental.getId();

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(java.util.Optional.of(completedRental));
        // When & Then
        Assertions.assertThrows(InvalidRentalStatusChangeException.class, () -> rentalService.updateRentalStatus(rentalId, RentalStatus.CANCELLED));
    }

    @Test
    @DisplayName("Test for getting rentals by renter ID")
    void shouldGetRentalsByRenterId() throws RenterNotFoundException {
        // Given
        Long renterId = 1L;
        List<Rental> expectedRentals = List.of(rental);

        Mockito.when(rentalRepository.findByRenterId(Mockito.anyLong())).thenReturn(expectedRentals);
        Mockito.when(renterRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(renter));
        // When
        List<Rental> result = rentalService.getRentalsByRenterId(renterId);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedRentals, result);
    }

    @Test
    @DisplayName("Test for calculating rental total price in PLN")
    void shouldDirectlyCalculateRentalTotalPriceInPln() throws Exception {
        // Given
        int rentalTierQuantity = 4;

        Method method = RentalService.class.getDeclaredMethod("calculateRentalTotalPrice", EquipmentPrice.class, Integer.class, CurrencyCode.class);
        method.setAccessible(true);
        // When
        BigDecimal result = (BigDecimal) method.invoke(rentalService, equipmentPrice, rentalTierQuantity, CurrencyCode.PLN);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("4.00"), result);
    }

    @Test
    @DisplayName("Test for calculating rental total price in EUR")
    void shouldDirectlyCalculateRentalTotalPriceInEur() throws Exception {
        // Given
        int rentalTierQuantity = 4;

        Mockito.when(apiNbpPlClient.getEuroExchangeRate()).thenReturn(exchangeRateDto);

        Method method = RentalService.class.getDeclaredMethod("calculateRentalTotalPrice", EquipmentPrice.class, Integer.class, CurrencyCode.class);
        method.setAccessible(true);
        // When
        BigDecimal result = (BigDecimal) method.invoke(rentalService, equipmentPrice, rentalTierQuantity, CurrencyCode.EUR);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("1.0000"), result);
    }
}
