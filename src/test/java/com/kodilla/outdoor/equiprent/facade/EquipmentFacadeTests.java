package com.kodilla.outdoor.equiprent.facade;

import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.domain.Tier;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentDto;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentPriceDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentPriceDto;
import com.kodilla.outdoor.equiprent.exception.ActiveEquipmentRentalException;
import com.kodilla.outdoor.equiprent.exception.CategoryNotFoundException;
import com.kodilla.outdoor.equiprent.exception.EquipmentNotFoundException;
import com.kodilla.outdoor.equiprent.mapper.EquipmentMapper;
import com.kodilla.outdoor.equiprent.mapper.FilterMapper;
import com.kodilla.outdoor.equiprent.service.EquipmentService;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for EquipmentFacade class")
@ExtendWith(MockitoExtension.class)
public class EquipmentFacadeTests {
    @InjectMocks
    private EquipmentFacade equipmentFacade;
    @Mock
    private EquipmentService equipmentService;
    @Mock
    private EquipmentMapper equipmentMapper;
    @Mock
    private FilterMapper filterMapper;

    @DisplayName("Test case for fetching no available equipment")
    @Test
    void shouldFetchEmptyList() throws Exception {
        // Given
        Mockito.when(filterMapper.mapStringToEquipmentCategoryList(Optional.empty())).thenReturn(List.of());
        Mockito.when(equipmentService.getEquipmentByCategories(Mockito.anyList())).thenReturn(List.of());
        Mockito.when(equipmentMapper.mapEquipmentListToEquipmentDtoList(Mockito.anyList())).thenReturn(List.of());
        // When
        List<EquipmentDto> result = equipmentFacade.getAllAvailableEquipment(Optional.empty());
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @DisplayName("Test case for fetching available equipment")
    @Test
    void shouldFetchAvailableEquipment() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPriceDto equipmentPriceDto = new EquipmentPriceDto(2L, "HOUR", new BigDecimal("1.00"));
        EquipmentDto equipmentDto = new EquipmentDto(1L, "Tent Plus", "Camping tent", "TENT", 3L,  new ArrayList<>(List.of(equipmentPriceDto)));

        Mockito.when(filterMapper.mapStringToEquipmentCategoryList(Optional.empty())).thenReturn(List.of());
        Mockito.when(equipmentService.getEquipmentByCategories(Mockito.anyList())).thenReturn(List.of(equipment));
        Mockito.when(equipmentMapper.mapEquipmentListToEquipmentDtoList(Mockito.anyList())).thenReturn(List.of(equipmentDto));
        // When
        List<EquipmentDto> result = equipmentFacade.getAllAvailableEquipment(Optional.empty());
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Tent Plus", result.get(0).getName());
    }

    @DisplayName("Test case for fetching equipment by ID")
    @Test
    void shouldFetchEquipmentById() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentDto equipmentDto = new EquipmentDto(1L, "Tent Plus", "Camping tent", "TENT", 3L, new ArrayList<>(List.of()));

        Mockito.when(equipmentService.getEquipmentById(Mockito.anyLong())).thenReturn(equipment);
        Mockito.when(equipmentMapper.mapEquipmentToEquipmentDto(Mockito.any())).thenReturn(equipmentDto);
        // When
        EquipmentDto result = equipmentFacade.getEquipmentById(1L);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Tent Plus", result.getName());
    }

    @DisplayName("Test case for fetching equipment by category")
    @Test
    void shouldFetchEquipmentByCategory() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentDto equipmentDto = new EquipmentDto(1L, "Tent Plus", "Camping tent", "TENT", 3L,  new ArrayList<>(List.of()));

        Mockito.when(equipmentService.getEquipmentByCategories(Mockito.any())).thenReturn(List.of(equipment));
        Mockito.when(filterMapper.mapStringToEquipmentCategoryList(Mockito.any())).thenReturn(List.of(EquipmentCategory.TENT));
        Mockito.when(equipmentMapper.mapEquipmentListToEquipmentDtoList(Mockito.anyList())).thenReturn(List.of(equipmentDto));
        // When
        List<EquipmentDto> result = equipmentFacade.getAllAvailableEquipment(Optional.of("tent"));
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Tent Plus", result.get(0).getName());
    }

    @DisplayName("Test case for handling CategoryNotFoundException")
    @Test
    void shouldHandleCategoryNotFoundException() throws Exception {
        // Given
        Mockito.when(filterMapper.mapStringToEquipmentCategoryList(Mockito.any())).thenThrow(new CategoryNotFoundException("unknown"));
        // When & Then
        Assertions.assertThrows(CategoryNotFoundException.class, () -> equipmentFacade.getAllAvailableEquipment(Optional.of("unknown")));
    }

    @DisplayName("Test case for fetching all categories")
    @Test
    void shouldFetchAllCategories() throws Exception {
        // Given
        Mockito.when(equipmentService.getAllCategories()).thenReturn(List.of(EquipmentCategory.TENT));
        // When
        List<EquipmentCategory> result = equipmentFacade.getAllCategories();
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(EquipmentCategory.TENT, result.get(0));
    }


    @Test
    @DisplayName("Test case for creating new equipment in existing category")
    void shouldCreateEquipmentInExistingCategory() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPriceDto equipmentPriceDto = new EquipmentPriceDto(2L, "HOUR", new BigDecimal("1.00"));
        EquipmentDto equipmentDto = new EquipmentDto(1L, "Tent Plus", "Camping tent", "TENT", 3L, new ArrayList<>(List.of(equipmentPriceDto)));

        CreateEquipmentPriceDto createEquipmentPriceDto = new CreateEquipmentPriceDto(Tier.HOUR, new BigDecimal("1.00"));
        CreateEquipmentDto createEquipmentDto = new CreateEquipmentDto("Tent Plus", "Camping tent", 3L, List.of(createEquipmentPriceDto));

        Mockito.when(equipmentMapper.mapCreateEquipmentDtoToEquipment(Mockito.any(), Mockito.any())).thenReturn(equipment);
        Mockito.when(equipmentMapper.mapEquipmentToEquipmentDto(Mockito.any())).thenReturn(equipmentDto);
        Mockito.when(equipmentService.addEquipment(Mockito.any())).thenReturn(equipment);
        // When
        EquipmentDto result = equipmentFacade.createEquipmentInCategory("tent", createEquipmentDto);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Tent Plus", result.getName());
    }

    @Test
    @DisplayName("Test case for handling CategoryNotFoundException when creating an equipment")
    void shouldHandleCategoryNotFound() throws Exception {
        // Given
        CreateEquipmentDto createEquipmentDto = new CreateEquipmentDto("Tent Plus", "Camping tent", 3L, List.of());

        Mockito.when(filterMapper.mapToCategoryOrThrow(Mockito.any())).thenThrow(new CategoryNotFoundException("unknown"));
        // When & Then
        CategoryNotFoundException thrown = Assertions.assertThrows(CategoryNotFoundException.class, () -> equipmentFacade.createEquipmentInCategory("tent", createEquipmentDto));
    }

    @Test
    @DisplayName("Test case for deleting an equipment")
    void shouldDeleteEquipment() throws Exception {
        // Given
        Mockito.when(filterMapper.mapToCategoryOrThrow(Mockito.any())).thenReturn(EquipmentCategory.TENT);
        Mockito.doNothing().when(equipmentService).updateEquipmentAvailability(Mockito.anyLong());
        // When
        equipmentFacade.deleteEquipmentFromCategory("tent", 1L);
        // Then
        Mockito.verify(equipmentService, Mockito.times(1)).updateEquipmentAvailability(Mockito.anyLong());
    }

    @Test
    @DisplayName("Test case for CategoryNotFoundException when deleting an equipment from non-existing category")
    void shouldHandleCategoryNotFoundExceptionWhenDeletingEquipment() throws Exception {
        // Given
        Mockito.when(filterMapper.mapToCategoryOrThrow(Mockito.any())).thenThrow(new CategoryNotFoundException("unknown"));
        // When & Then
        Assertions.assertThrows(CategoryNotFoundException.class, () -> equipmentFacade.deleteEquipmentFromCategory("tent", 1L));
    }

    @Test
    @DisplayName("Test case for EquipmentNotFoundException when deleting a non-existing equipment")
    void shouldHandleEquipmentNotFoundExceptionWhenDeletingEquipment() throws Exception {
        // Given
        Mockito.when(filterMapper.mapToCategoryOrThrow(Mockito.any())).thenReturn(EquipmentCategory.TENT);
        Mockito.doThrow(new EquipmentNotFoundException(1L)).when(equipmentService).updateEquipmentAvailability(Mockito.anyLong());
        // When & Then
        Assertions.assertThrows(EquipmentNotFoundException.class, () -> equipmentFacade.deleteEquipmentFromCategory("tent", 1L));
    }

    @Test
    @DisplayName("Test case for ActiveEquipmentRentalException when deleting an equipment with active rentals")
    void shouldHandleActiveEquipmentRentalExceptionWhenDeletingEquipment() throws Exception {
        // Given
        Mockito.when(filterMapper.mapToCategoryOrThrow(Mockito.any())).thenReturn(EquipmentCategory.TENT);
        Mockito.doThrow(new ActiveEquipmentRentalException(1L)).when(equipmentService).updateEquipmentAvailability(Mockito.anyLong());
        // When & Then
        ActiveEquipmentRentalException thrown = Assertions.assertThrows(ActiveEquipmentRentalException.class, () -> equipmentFacade.deleteEquipmentFromCategory("tent", 1L));
    }
}