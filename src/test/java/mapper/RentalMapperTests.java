package mapper;

import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.dto.Invoice;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import com.kodilla.outdoor.equiprent.mapper.RentalMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Tests for RentalMapper class")
@ExtendWith(MockitoExtension.class)
public class RentalMapperTests {
    @InjectMocks
    private RentalMapper rentalMapper;

    @DisplayName("Test case for mapRentalToRentalDto method")
    @Test
    void shouldReturnRentalDto() {
        // Given
        Renter renter = new Renter(1L, "John", "Doe", "john.doe@example.com", "123456789", "Main Street 1", LocalDateTime.now(), null);
        Equipment equipment = new Equipment(2L, "Tent", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        Rental rental = new Rental(3L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 1), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("200.00"), CurrencyCode.PLN, LocalDateTime.now(), null);
        // When
        RentalDto result = rentalMapper.mapRentalToRentalDto(rental);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3L, result.getId());
        Assertions.assertEquals(2L, result.getEquipmentId());
        Assertions.assertEquals(1L, result.getRenterId());
        Assertions.assertEquals("2024-09-21T12:00:01", result.getRentalStart());
        Assertions.assertEquals("2024-09-21T15:00", result.getRentalEnd());
        Assertions.assertEquals("ACTIVE", result.getStatus());
        Assertions.assertEquals(new BigDecimal("200.00"), result.getTotalPrice());
        Assertions.assertEquals("PLN", result.getCurrencyCode());
    }

    @DisplayName("Test case for mapRentalListToRentalDtoList method")
    @Test
    void shouldReturnRentalDtoList() {
        // Given
        Renter renter = new Renter(1L, "John", "Doe", "john.doe@example.com", "123456789", "Main Street 1", LocalDateTime.now(), null);
        Equipment equipment = new Equipment(2L, "Tent", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        Rental rental = new Rental(3L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 0), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("200.00"), CurrencyCode.PLN, LocalDateTime.now(), null);
        List<Rental> rentals = List.of(rental);
        // When
        List<RentalDto> result = rentalMapper.mapRentalListToRentalDtoList(rentals);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @DisplayName("Test case for mapRentalToInvoice method")
    @Test
    void shouldReturnInvoice() {
        // Given
        Renter renter = new Renter(1L, "John", "Doe", "john.doe@example.com", "123456789", "Main Street 1", LocalDateTime.now(), null);
        Equipment equipment = new Equipment(2L, "Tent", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        Rental rental = new Rental(3L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 0), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("200.00"), CurrencyCode.PLN, LocalDateTime.now(), null);
        // When
        Invoice result = rentalMapper.mapRentalToInvoice(rental);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(rental.getRentalStart(), result.getRentalStartDate());
        Assertions.assertEquals(rental.getRentalEnd(), result.getRentalEndDate());
        Assertions.assertEquals(rental.getCreationDate(), result.getCreationDate());
        Assertions.assertEquals(renter, result.getRenter());
        Assertions.assertEquals(equipment, result.getEquipment());
        Assertions.assertEquals(rental.getTotalPrice(), result.getTotalPrice());
        Assertions.assertEquals(rental.getCurrencyCode(), result.getCurrencyCode());
    }
}
