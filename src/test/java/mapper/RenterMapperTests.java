package mapper;

import com.kodilla.outdoor.equiprent.domain.Renter;
import com.kodilla.outdoor.equiprent.dto.CreateUpdateRenterDto;
import com.kodilla.outdoor.equiprent.dto.RenterDto;
import com.kodilla.outdoor.equiprent.mapper.RenterMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@DisplayName("Tests for RenterMapper class")
@ExtendWith(MockitoExtension.class)
public class RenterMapperTests {
    @InjectMocks
    private RenterMapper renterMapper;

    @DisplayName("Test case for mapRenterToRenterDto method")
    @Test
    void shouldReturnRenterDto() {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubus@bubuslawski.com", "123456789", "Bubuslawska 1", null, null);
        // When
        RenterDto result = renterMapper.mapRenterToRenterDto(renter);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Bubuslaw", result.getFirstName());
        Assertions.assertEquals("Bubuslawski", result.getLastName());
        Assertions.assertEquals("bubus@bubuslawski.com", result.getEmail());
        Assertions.assertEquals("123456789", result.getPhoneNumber());
        Assertions.assertEquals("Bubuslawska 1", result.getAddress());
    }

    @DisplayName("Test case for mapCreateUpdateRenterDtoToRenter method")
    @Test
    void shouldReturnRenter() {
        // Given
        CreateUpdateRenterDto createUpdateRenterDto = new CreateUpdateRenterDto("Bubuslaw", "Bubuslawski", "bubus@bubuslawski.com", "123456789", "Bubuslawska 1");
        // When
        Renter result = renterMapper.mapCreateUpdateRenterDtoToRenter(createUpdateRenterDto);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getId());
        Assertions.assertEquals("Bubuslaw", result.getFirstName());
        Assertions.assertEquals("Bubuslawski", result.getLastName());
        Assertions.assertEquals("bubus@bubuslawski.com", result.getEmail());
        Assertions.assertEquals("123456789", result.getPhoneNumber());
        Assertions.assertEquals("Bubuslawska 1", result.getAddress());
        Assertions.assertNull(result.getCreationDate());
        Assertions.assertNull(result.getModificationDate());
    }

    @DisplayName("Test case for mapRenterListToRenterDtoList method")
    @Test
    void shouldReturnRenterDtoList() {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubus@bubuslawski.com", "123456789", "Bubuslawska 1", null, null);
        List<Renter> renterList = List.of(renter);
        // When
        List<RenterDto> result = renterMapper.mapRenterListToRenterDtoList(renterList);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals("Bubuslaw", result.get(0).getFirstName());
        Assertions.assertEquals("Bubuslawski", result.get(0).getLastName());
        Assertions.assertEquals("bubus@bubuslawski.com", result.get(0).getEmail());
        Assertions.assertEquals("123456789", result.get(0).getPhoneNumber());
        Assertions.assertEquals("Bubuslawska 1", result.get(0).getAddress());
    }
}
