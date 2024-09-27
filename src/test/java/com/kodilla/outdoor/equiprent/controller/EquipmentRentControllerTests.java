package com.kodilla.outdoor.equiprent.controller;

import com.google.gson.Gson;
import com.kodilla.outdoor.equiprent.exception.*;
import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.dto.CreateRentalDto;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import com.kodilla.outdoor.equiprent.mapper.FilterMapper;
import com.kodilla.outdoor.equiprent.mapper.RentalMapper;
import com.kodilla.outdoor.equiprent.service.RentalService;
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
import java.util.List;

@DisplayName("Tests for EquipmentRentController class")
@SpringJUnitWebConfig
@WebMvcTest(EquipmentRentController.class)
public class EquipmentRentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RentalService rentalService;
    @MockBean
    private RentalMapper rentalMapper;
    @MockBean
    private FilterMapper filterMapper;

    @DisplayName("Test case for creating a rental")
    @Test
    void shouldCreateRental() throws Exception {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        CreateRentalDto createRentalDto = new CreateRentalDto(1L, 1L, 2L, 3);
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 24, 13, 0, 0));
        Rental rental = new Rental(1L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 0), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("211.11"), LocalDateTime.of(2024, 9, 21, 12, 0, 0), null);
        RentalDto rentalDto = new RentalDto(1L, 1L, 1L, "2023-09-21T12:00:00", "2023-09-21T15:00:00", "ACTIVE", new BigDecimal("211.11"));

        Mockito.when(rentalService.createRental(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(rental);
        Mockito.when(rentalMapper.mapRentalToRentalDto(rental)).thenReturn(rentalDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createRentalDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.equipmentId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.renterId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalStart", Matchers.is("2023-09-21T12:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalEnd", Matchers.is("2023-09-21T15:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("ACTIVE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice", Matchers.is(211.11)));
    }

    @DisplayName("Test case for handling EquipmentNotFoundException when creating a rental")
    @Test
    void shouldHandleEquipmentNotFoundException() throws Exception {
        // Given
        CreateRentalDto createRentalDto = new CreateRentalDto(1L, 1L, 2L, 3);

        Mockito.when(rentalService.createRental(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new EquipmentNotFoundException(1L));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createRentalDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("equipment.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Equipment with ID 1 not found.")));
    }

    @DisplayName("Test case for handling EquipmentNotAvailableException when creating a rental")
    @Test
    void shouldHandleEquipmentNotAvailableException() throws Exception {
        // Given
        CreateRentalDto createRentalDto = new CreateRentalDto(1L, 1L, 2L, 3);

        Mockito.when(rentalService.createRental(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new EquipmentNotAvailableException(1L));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createRentalDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("equipment.not.available")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Equipment with ID 1 not available.")));
    }

    @DisplayName("Test case for handling TierNotAvailableException when creating a rental")
    @Test
    void shouldHandleTierNotAvailableException() throws Exception {
        // Given
        CreateRentalDto createRentalDto = new CreateRentalDto(1L, 1L, 2L, 3);

        Mockito.when(rentalService.createRental(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new TierNotAvailableException(2L));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createRentalDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("rental.tier.not.available")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Rental tier with ID 2 not available.")));
    }

    @DisplayName("Test case for handling RenterNotFoundException when creating a rental")
    @Test
    void shouldHandleRenterNotFoundException() throws Exception {
        // Given
        CreateRentalDto createRentalDto = new CreateRentalDto(1L, 1L, 2L, 3);

        Mockito.when(rentalService.createRental(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new RenterNotFoundException(1L));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createRentalDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("renter.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Renter with ID 1 not found.")));
    }

    @DisplayName("Test case for fetching all rentals")
    @Test
    void shouldFetchAllRentals() throws Exception {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 21, 15, 0, 0));
        Rental rental = new Rental(1L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 0), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("211.11"), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null);
        RentalDto rentalDto = new RentalDto(1L, 1L, 1L, "2023-09-21T12:00:00", "2023-09-21T15:00:00", "ACTIVE", new BigDecimal("211.11"));

        Mockito.when(rentalService.getRentalByStatuses(List.of())).thenReturn(List.of(rental));
        Mockito.when(rentalMapper.mapRentalListToRentalDtoList(List.of(rental))).thenReturn(List.of(rentalDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].equipmentId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].renterId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalStart", Matchers.is("2023-09-21T12:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalEnd", Matchers.is("2023-09-21T15:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("ACTIVE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", Matchers.is(211.11)));
    }

    @DisplayName("Test case for fetching rentals by status")
    @Test
    void shouldFetchRentalsByStatus() throws Exception {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        RentalDto rentalDto = new RentalDto(1L, 1L, 1L, "2023-09-21T12:00:00", "2023-09-21T15:00:00", "ACTIVE", new BigDecimal("211.11"));
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 21, 15, 0, 0));
        Rental rental = new Rental(1L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 0), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.ACTIVE, new BigDecimal("211.11"), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null);

        Mockito.when(filterMapper.mapStringToRentalStatusList(Mockito.any())).thenReturn(List.of(RentalStatus.ACTIVE));
        Mockito.when(rentalService.getRentalByStatuses(Mockito.anyList())).thenReturn(List.of(rental));
        Mockito.when(rentalMapper.mapRentalListToRentalDtoList(Mockito.anyList())).thenReturn(List.of(rentalDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rentals?status=ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].equipmentId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].renterId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalStart", Matchers.is("2023-09-21T12:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalEnd", Matchers.is("2023-09-21T15:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("ACTIVE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", Matchers.is(211.11)));
    }

    @DisplayName("Test case for handling RentalStatusNotFoundException")
    @Test
    void shouldHandleRentalStatusNotFoundException() throws Exception {
        // Given
        Mockito.when(filterMapper.mapStringToRentalStatusList(Mockito.any())).thenThrow(new RentalStatusNotFoundException("INVALID_STATUS"));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rentals?status=INVALID_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("rental.status.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Rental status INVALID_STATUS not found.")));
    }

    @DisplayName("Should update rental status to COMPLETED")
    @Test
    void shouldUpdateRentalStatusToCompleted() throws Exception {
        // Given
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@test.pl", "000000000", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 14, 0, 0), null);
        RentalDto rentalDto = new RentalDto(1L, 1L, 1L, "2023-09-21T12:00:00", "2023-09-21T15:00:00", "COMPLETED", new BigDecimal("211.11"));
        Equipment equipment = new Equipment(1L, "Tent Plus", "Camping tent", EquipmentCategory.TENT, null, null, LocalDateTime.of(2024, 9, 21, 15, 0, 0));
        Rental rental = new Rental(1L, equipment, renter, LocalDateTime.of(2024, 9, 21, 12, 0, 0), LocalDateTime.of(2024, 9, 21, 15, 0, 0), null, RentalStatus.COMPLETED, new BigDecimal("211.11"), LocalDateTime.of(2024, 9, 21, 15, 0, 0), LocalDateTime.of(2024, 9, 21, 16, 0, 0));

        Mockito.when(rentalService.updateRentalStatus(1L, RentalStatus.COMPLETED)).thenReturn(rental);
        Mockito.when(rentalMapper.mapRentalToRentalDto(Mockito.any())).thenReturn(rentalDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/rentals/1/status?status=COMPLETED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.equipmentId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.renterId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalStart", Matchers.is("2023-09-21T12:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalEnd", Matchers.is("2023-09-21T15:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("COMPLETED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice", Matchers.is(211.11)));
    }

    @DisplayName("Test case for handling InvalidRentalStatusException when updating rental status")
    @Test
    void shouldHandleInvalidRentalStatusExceptionWhenRentalStatusIsNotActive() throws Exception {
        // Given
        Mockito.when(rentalService.updateRentalStatus(Mockito.any(), Mockito.any()))
                .thenThrow(new InvalidRentalStatusChangeException(RentalStatus.COMPLETED, RentalStatus.CANCELLED));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/rentals/1/status?status=COMPLETED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("rental.status.not.available")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Rental status change from CANCELLED to COMPLETED is invalid.")));
    }

    @DisplayName("Test case for handling RentalNotFoundException when updating rental status")
    @Test
    void shouldHandleRentalNotFoundExceptionWhenRentalIsNotFound() throws Exception {
        // Given
        Mockito.when(rentalService.updateRentalStatus(Mockito.any(), Mockito.any()))
                .thenThrow(new RentalNotFoundException(1L));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/rentals/1/status?status=COMPLETED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("rental.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Rental with ID 1 not found.")));
    }

    @DisplayName("Test case for handling RentalStatusNotFoundException when updating rental status")
    @Test
    void shouldHandleRentalStatusNotFoundExceptionWhenRentalStatusIsNotFound() throws Exception {
        // Given
        Mockito.when(filterMapper.mapToStatusOrThrow(Mockito.any()))
                .thenThrow(new RentalStatusNotFoundException("NOT_EXIST_STATUS"));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/rentals/1/status?status=NOT_EXIST_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("rental.status.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Rental status NOT_EXIST_STATUS not found.")));
    }
}
