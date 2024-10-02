package mapper;

import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentDto;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentPriceDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentDto;
import com.kodilla.outdoor.equiprent.mapper.EquipmentMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Tests for EquipmentMapper class")
@ExtendWith(MockitoExtension.class)
public class EquipmentMapperTests {
    @InjectMocks
    private EquipmentMapper equipmentMapper;

    @DisplayName("Test case for mapEquipmentListToEquipmentDtoList method")
    @Test
    void shouldReturnEquipmentDtoList() {
        // Given
        Equipment equipment = new Equipment(2L, "name", "description", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(1L, null, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(3L, null, Tier.HOUR, new BigDecimal("11.11"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipment.setEquipmentAvailability(equipmentAvailability);
        equipment.setPrices(List.of(equipmentPrice));

        List<Equipment> equipmentList = List.of(equipment);
        // When
        List<EquipmentDto> result = equipmentMapper.mapEquipmentListToEquipmentDtoList(equipmentList);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @DisplayName("Test case for mapEquipmentToEquipmentDto method")
    @Test
    void shouldReturnEquipmentDto() {
        // Given
        Equipment equipment = new Equipment(2L, "name", "description", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentAvailability equipmentAvailability = new EquipmentAvailability(1L, null, 10L, 5L, true, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(3L, null, Tier.HOUR, new BigDecimal("11.11"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        equipment.setEquipmentAvailability(equipmentAvailability);
        equipment.setPrices(List.of(equipmentPrice));
        // When
        EquipmentDto result = equipmentMapper.mapEquipmentToEquipmentDto(equipment);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2L, result.getId());
        Assertions.assertEquals("TENT", result.getCategory());
        Assertions.assertEquals("name", result.getName());
        Assertions.assertEquals("description", result.getDescription());
        Assertions.assertEquals(5L, result.getCurrentQuantity());
        Assertions.assertEquals(1, result.getPrices().size());
    }

    @DisplayName("Test case for mapCreateEquipmentPriceDtoListToEquipmentPriceList method")
    @Test
    void shouldReturnEquipmentPriceList() {
        // Given
        CreateEquipmentPriceDto createEquipmentPriceDto = new CreateEquipmentPriceDto(Tier.HOUR, new BigDecimal("11.11"));
        List<CreateEquipmentPriceDto> createEquipmentPriceDtoList = List.of(createEquipmentPriceDto);
        Equipment equipment = new Equipment(2L, "name", "description", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        // When
        List<EquipmentPrice> result = equipmentMapper.mapCreateEquipmentPriceDtoListToEquipmentPriceList(equipment, createEquipmentPriceDtoList);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(new BigDecimal("11.11"), result.get(0).getPrice());
        Assertions.assertEquals(Tier.HOUR, result.get(0).getTier());
    }

    @DisplayName("Test case for mapCreateEquipmentPriceDtoToEquipmentPrice method")
    @Test
    void shouldMapCreateEquipmentPriceDtoToEquipmentPrice() {
        // Given
        CreateEquipmentPriceDto createEquipmentPriceDto = new CreateEquipmentPriceDto(Tier.DAY, new BigDecimal("25.00"));
        Equipment equipment = new Equipment(2L, "name", "description", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        // When
        EquipmentPrice result = equipmentMapper.mapCreateEquipmentPriceDtoToEquipmentPrice(equipment, createEquipmentPriceDto);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("25.00"), result.getPrice());
        Assertions.assertEquals(Tier.DAY, result.getTier());
        Assertions.assertEquals(equipment, result.getEquipment());
    }

    @DisplayName("Test case for mapCurrentQuantityToEquipmentAvailability method")
    @Test
    void shouldMapCurrentQuantityToEquipmentAvailability() {
        // Given
        Equipment equipment = new Equipment(2L, "name", "description", EquipmentCategory.TENT, null, null, LocalDateTime.now());
        Long currentQuantity = 10L;
        // When
        EquipmentAvailability result = equipmentMapper.mapCurrentQuantityToEquipmentAvailability(equipment, currentQuantity);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(equipment, result.getEquipment());
        Assertions.assertEquals(currentQuantity, result.getCurrentQuantity());
        Assertions.assertEquals(currentQuantity, result.getInitialQuantity());
    }

    @DisplayName("Test case for mapCreateEquipmentDtoToEquipment method")
    @Test
    void shouldMapCreateEquipmentDtoToEquipment() {
        // Given
        CreateEquipmentDto createEquipmentDto = new CreateEquipmentDto("Tent", "Tent Plus", 10L, List.of(new CreateEquipmentPriceDto(Tier.HOUR, new BigDecimal("15.00"))));
        // When
        Equipment result = equipmentMapper.mapCreateEquipmentDtoToEquipment(EquipmentCategory.TENT, createEquipmentDto);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Tent", result.getName());
        Assertions.assertEquals("Tent Plus", result.getDescription());
        Assertions.assertEquals(EquipmentCategory.TENT, result.getCategory());
        Assertions.assertEquals(10L, result.getEquipmentAvailability().getCurrentQuantity());
        Assertions.assertEquals(1, result.getPrices().size());
        Assertions.assertEquals(new BigDecimal("15.00"), result.getPrices().get(0).getPrice());
    }
}