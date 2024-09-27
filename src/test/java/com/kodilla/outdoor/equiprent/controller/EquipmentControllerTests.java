package com.kodilla.outdoor.equiprent.controller;

import com.google.gson.Gson;
import com.kodilla.outdoor.equiprent.exception.CategoryNotFoundException;
import com.kodilla.outdoor.equiprent.controller.filter.FilterMapper;
import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentDto;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentPriceDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentPriceDto;
import com.kodilla.outdoor.equiprent.mapper.EquipmentMapper;
import com.kodilla.outdoor.equiprent.service.EquipmentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Tests for EquipmentController class")
@SpringJUnitWebConfig
@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EquipmentService equipmentService;
    @MockBean
    private EquipmentMapper equipmentMapper;
    @MockBean
    private FilterMapper filterMapper;

    @DisplayName("Test case for fetching no available equipment")
    @Test
    void shouldFetchEmptyList() throws Exception {
        // Given
        Mockito.when(equipmentService.getEquipmentByCategories(List.of())).thenReturn(List.of());
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @DisplayName("Test case for fetching available equipment")
    @Test
    void shouldFetchAvailableEquipment() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(2L, equipment, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPriceDto equipmentPriceDto = new EquipmentPriceDto(2L, "HOUR", new BigDecimal("1.00"));
        EquipmentDto equipmentDto = new EquipmentDto(1L, "Tent Plus", "Camping tent", "TENT", 3L,  new ArrayList<>(List.of(equipmentPriceDto)));

        Mockito.when(equipmentService.getEquipmentByCategories(List.of())).thenReturn(List.of(equipment));
        Mockito.when(equipmentMapper.mapEquipmentListToEquipmentDtoList(Mockito.anyList())).thenReturn(List.of(equipmentDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Tent Plus")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("Camping tent")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category", Matchers.is("TENT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].availableQuantity", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prices[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prices[0].priceTier", Matchers.is("HOUR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prices[0].price", Matchers.is(1.00)));
    }

    @DisplayName("Test case for fetching equipment by category")
    @Test
    void shouldFetchEquipmentByCategory() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPrice equipmentPrice = new EquipmentPrice(2L, equipment, Tier.HOUR, new BigDecimal("1.00"), LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        EquipmentPriceDto equipmentPriceDto = new EquipmentPriceDto(2L, "HOUR", new BigDecimal("1.00"));
        EquipmentDto equipmentDto = new EquipmentDto(1L, "Tent Plus", "Camping tent", "TENT", 3L, new ArrayList<>(List.of(equipmentPriceDto)));

        Mockito.when(filterMapper.mapStringToEquipmentCategoryList(Mockito.any())).thenReturn(List.of(EquipmentCategory.TENT));
        Mockito.when(equipmentService.getEquipmentByCategories(Mockito.anyList())).thenReturn(List.of(equipment));
        Mockito.when(equipmentMapper.mapEquipmentListToEquipmentDtoList(Mockito.anyList())).thenReturn(List.of(equipmentDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment?category=TENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Tent Plus")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("Camping tent")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category", Matchers.is("TENT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].availableQuantity", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prices[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prices[0].priceTier", Matchers.is("HOUR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prices[0].price", Matchers.is(1.00)));
    }

    @DisplayName("Test case for handling CategoryNotFoundException")
    @Test
    void shouldHandleCategoryNotFoundException() throws Exception {
        // Given
        Mockito.when(filterMapper.mapStringToEquipmentCategoryList(Mockito.any())).thenThrow(new CategoryNotFoundException("NOT_EXIST"));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment?category=NOT_EXIST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("category.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Category NOT_EXIST not found.")));
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

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createEquipmentDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/equipment/categories/tent/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Tent Plus")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Camping tent")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", Matchers.is("TENT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availableQuantity", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prices[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prices[0].priceTier", Matchers.is("HOUR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prices[0].price", Matchers.is(1.00)));
    }

    @Test
    @DisplayName("Test case for handling CategoryNotFoundException")
    void shouldHandleCategoryNotFound() throws Exception {
        // Given
        CreateEquipmentDto createEquipmentDto = new CreateEquipmentDto("Tent Plus", "Camping tent", 3L, List.of());

        Mockito.when(filterMapper.mapToCategoryOrThrow(Mockito.any())).thenThrow(new CategoryNotFoundException("UNKNOWN"));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createEquipmentDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/equipment/categories/unknown/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("category.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Category UNKNOWN not found.")));
    }
}