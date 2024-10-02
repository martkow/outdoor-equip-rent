package mapper;

import com.kodilla.outdoor.equiprent.domain.CurrencyCode;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.domain.RentalStatus;
import com.kodilla.outdoor.equiprent.exception.CategoryNotFoundException;
import com.kodilla.outdoor.equiprent.exception.CurrencyCodeNotFoundException;
import com.kodilla.outdoor.equiprent.exception.RentalStatusNotFoundException;
import com.kodilla.outdoor.equiprent.mapper.FilterMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@DisplayName("Tests for FilterMapper class")
@ExtendWith(MockitoExtension.class)
public class FilterMapperTests {
    @InjectMocks
    private FilterMapper filterMapper;

    @DisplayName("Test case for mapStringToEquipmentCategoryList method with valid category")
    @Test
    void shouldReturnEquipmentCategoryList() throws CategoryNotFoundException {
        // Given
        Optional<String> category = Optional.of("TENT,BACKPACK");
        // When
        List<EquipmentCategory> result = filterMapper.mapStringToEquipmentCategoryList(category);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(EquipmentCategory.TENT));
        Assertions.assertTrue(result.contains(EquipmentCategory.BACKPACK));
    }

    @DisplayName("Test case for mapStringToEquipmentCategoryList method with invalid category")
    @Test
    void shouldThrowCategoryNotFoundException() {
        // Given
        Optional<String> category = Optional.of("INVALID_CATEGORY");
        // When & Then
        Assertions.assertThrows(CategoryNotFoundException.class, () -> filterMapper.mapStringToEquipmentCategoryList(category));
    }

    @DisplayName("Test case for mapStringToRentalStatusList method with valid status")
    @Test
    void shouldReturnRentalStatusList() throws RentalStatusNotFoundException {
        // Given
        Optional<String> status = Optional.of("ACTIVE,COMPLETED");
        // When
        List<RentalStatus> result = filterMapper.mapStringToRentalStatusList(status);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(RentalStatus.ACTIVE));
        Assertions.assertTrue(result.contains(RentalStatus.COMPLETED));
    }

    @DisplayName("Test case for mapStringToRentalStatusList method with invalid status")
    @Test
    void shouldThrowRentalStatusNotFoundExceptionWhenMappingStringToRentalStatusList() {
        // Given
        Optional<String> status = Optional.of("INVALID_STATUS");
        // When / Then
        Assertions.assertThrows(RentalStatusNotFoundException.class, () -> filterMapper.mapStringToRentalStatusList(status));
    }

    @DisplayName("Test case for mapToStatusOrThrow with valid status")
    @Test
    void shouldReturnRentalStatus() throws RentalStatusNotFoundException {
        // Given
        String status = "ACTIVE";
        // When
        RentalStatus result = filterMapper.mapToStatusOrThrow(status);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(RentalStatus.ACTIVE, result);
    }

    @DisplayName("Test case for mapToStatusOrThrow with invalid status")
    @Test
    void shouldThrowRentalStatusNotFoundException() {
        // Given
        String status = "INVALID_STATUS";
        // When / Then
        Assertions.assertThrows(RentalStatusNotFoundException.class, () -> filterMapper.mapToStatusOrThrow(status));
    }

    @DisplayName("Test case for mapToStatusOrThrow with lowercase valid status")
    @Test
    void shouldReturnRentalStatusForLowercaseInput() throws RentalStatusNotFoundException {
        // Given
        String status = "completed";
        // When
        RentalStatus result = filterMapper.mapToStatusOrThrow(status);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(RentalStatus.COMPLETED, result);
    }

    @DisplayName("Test case for mapToCurrencyCodeOrThrow method with valid currency")
    @Test
    void shouldReturnCurrencyCode() throws CurrencyCodeNotFoundException {
        // Given
        Optional<String> currencyCode = Optional.of("EUR");
        // When
        CurrencyCode result = filterMapper.mapToCurrencyCodeOrThrow(currencyCode);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(CurrencyCode.EUR, result);
    }

    @DisplayName("Test case for mapToCurrencyCodeOrThrow method with invalid currency")
    @Test
    void shouldThrowCurrencyCodeNotFoundException() {
        // Given
        Optional<String> currencyCode = Optional.of("INVALID_CODE");
        // When & Then
        Assertions.assertThrows(CurrencyCodeNotFoundException.class, () -> filterMapper.mapToCurrencyCodeOrThrow(currencyCode));
    }

    @DisplayName("Test case for mapToCurrencyCodeOrThrow method with empty currency (default to PLN)")
    @Test
    void shouldReturnDefaultCurrencyCode() throws CurrencyCodeNotFoundException {
        // Given
        Optional<String> currencyCode = Optional.empty();
        // When
        CurrencyCode result = filterMapper.mapToCurrencyCodeOrThrow(currencyCode);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(CurrencyCode.PLN, result);
    }
}
