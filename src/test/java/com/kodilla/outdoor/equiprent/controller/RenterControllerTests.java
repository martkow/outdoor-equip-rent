package com.kodilla.outdoor.equiprent.controller;

import com.google.gson.Gson;
import com.kodilla.outdoor.equiprent.controller.exception.RenterAlreadyExistsException;
import com.kodilla.outdoor.equiprent.controller.exception.RenterNotFoundException;
import com.kodilla.outdoor.equiprent.domain.Renter;
import com.kodilla.outdoor.equiprent.dto.CreateUpdateRenterDto;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import com.kodilla.outdoor.equiprent.dto.RenterDto;
import com.kodilla.outdoor.equiprent.mapper.RentalMapper;
import com.kodilla.outdoor.equiprent.mapper.RenterMapper;
import com.kodilla.outdoor.equiprent.service.RentalService;
import com.kodilla.outdoor.equiprent.service.RenterService;
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

@DisplayName("Tests for RenterController")
@SpringJUnitWebConfig
@WebMvcTest(RenterController.class)
public class RenterControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RenterService renterService;
    @MockBean
    private RenterMapper renterMapper;
    @MockBean
    private RentalService rentalService;
    @MockBean
    private RentalMapper rentalMapper;

    @Test
    @DisplayName("Test for creating a new renter")
    void shouldCreateRenter() throws Exception {
        // Given
        CreateUpdateRenterDto createUpdateRenterDto = new CreateUpdateRenterDto("Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1");
        RenterDto renterDto = new RenterDto(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1");
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 17, 0, 0), null);

        Mockito.when(renterMapper.mapCreateUpdateRenterDtoToRenter(createUpdateRenterDto)).thenReturn(renter);
        Mockito.when(renterService.createRenter(Mockito.any())).thenReturn(renter);
        Mockito.when(renterMapper.mapRenterToRenterDto(Mockito.any())).thenReturn(renterDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createUpdateRenterDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/renters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Bubuslaw")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Bubuslawski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("bubuslaw@bubuslawski.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("Bubuslawska 1")));
    }

    @Test
    @DisplayName("Test case for RenterAlreadyExistsException when creating renter with already existing email")
    void shouldHandleRenterAlreadyExistsException() throws Exception {
        // Given
        CreateUpdateRenterDto createUpdateRenterDto = new CreateUpdateRenterDto("Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1");

        Mockito.when(renterService.createRenter(Mockito.any())).thenThrow(new RenterAlreadyExistsException("bubuslaw@bubuslawski.com"));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createUpdateRenterDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/renters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("renter.already.exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Renter with email bubuslaw@bubuslawski.com already exists.")));
    }

    @Test
    @DisplayName("Test for updating an existing renter")
    void shouldUpdateRenter() throws Exception {
        // Given
        Long renterId = 1L;
        CreateUpdateRenterDto updateRenterDto = new CreateUpdateRenterDto("Bubuslaw", "Bubuslawski", "updated@bubuslawski.com", "987654321", "Updated Address 1");
        Renter renter = new Renter(null, "Bubuslaw", "Bubuslawski", "updated@bubuslawski.com", "987654321", "Updated Address 1", null, null);

        Mockito.when(renterMapper.mapCreateUpdateRenterDtoToRenter(updateRenterDto)).thenReturn(renter);
        Mockito.doNothing().when(renterService).updateRenter(renterId, renter);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(updateRenterDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/renters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test case for RenterNotFoundException when updating non-existing renter")
    void shouldHandleRenterNotFoundExceptionWhenUpdating() throws Exception {
        // Given
        Long renterId = 1L;
        CreateUpdateRenterDto updateRenterDto = new CreateUpdateRenterDto("Bubuslaw", "Bubuslawski", "updated@bubuslawski.com", "987654321", "Updated Address 1");

        Mockito.doThrow(new RenterNotFoundException(renterId)).when(renterService).updateRenter(Mockito.eq(renterId), Mockito.any());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(updateRenterDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/renters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // 400 status code for bad request
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("renter.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Renter with ID " + renterId + " not found.")));
    }


    @Test
    @DisplayName("Test for retrieving all renters")
    void shouldGetAllRenters() throws Exception {
        // Given
        RenterDto renterDto = new RenterDto(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1");
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 17, 0, 0), null);

        Mockito.when(renterMapper.mapRenterListToRenterDtoList(Mockito.anyList())).thenReturn(List.of(renterDto));
        Mockito.when(renterService.getAllRenters()).thenReturn(List.of(renter));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/renters")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is("Bubuslaw")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("Bubuslawski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("bubuslaw@bubuslawski.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address", Matchers.is("Bubuslawska 1")));
    }

    @Test
    @DisplayName("Test for retrieving renter by ID")
    void shouldGetRenterById() throws Exception {
        // Given
        Long renterId = 1L;
        RenterDto renterDto = new RenterDto(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1");
        Renter renter = new Renter(1L, "Bubuslaw", "Bubuslawski", "bubuslaw@bubuslawski.com", "123456789", "Bubuslawska 1", LocalDateTime.of(2024, 9, 24, 17, 0, 0), null);

        Mockito.when(renterService.getRenter(renterId)).thenReturn(renter);
        Mockito.when(renterMapper.mapRenterToRenterDto(Mockito.any())).thenReturn(renterDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/renters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Bubuslaw")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Bubuslawski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("bubuslaw@bubuslawski.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("Bubuslawska 1")));
    }

    @Test
    @DisplayName("Test case for RenterNotFoundException when retrieving non-existing renter")
    void shouldHandleRenterNotFoundExceptionException() throws Exception {
        // Given
        Long renterId = 1L;

        Mockito.when(renterService.getRenter(renterId)).thenThrow(new RenterNotFoundException(renterId));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/renters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("renter.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Renter with ID 1 not found.")));
    }

    @Test
    @DisplayName("Test for retrieving renter's rentals by renter ID")
    void shouldGetRentalsByRenterId() throws Exception {
        // Given
        Long renterId = 1L;
        RentalDto rentalDto = new RentalDto(1L, 2L, 3L, "2024-09-24T10:00:00", "2024-09-24T12:00:00", "ACTIVE", new BigDecimal("11.11"));

        Mockito.when(renterService.getRenter(renterId)).thenReturn(new Renter());
        Mockito.when(rentalService.getRentalsByRenterId(renterId)).thenReturn(List.of());
        Mockito.when(rentalMapper.mapRentalListToRentalDtoList(Mockito.anyList())).thenReturn(List.of(rentalDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/renters/1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].equipmentId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].renterId", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalStart", Matchers.is("2024-09-24T10:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalEnd", Matchers.is("2024-09-24T12:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("ACTIVE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", Matchers.is(11.11)));
    }

    @Test
    @DisplayName("Test case for RenterNotFoundException when retrieving rentals for non-existing renter")
    void shouldHandleRenterNotFoundExceptionExceptionWhenRetrievingRentals() throws Exception {
        // Given
        Long renterId = 1L;

        Mockito.when(renterService.getRenter(renterId)).thenThrow(new RenterNotFoundException(renterId));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/renters/1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("renter.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Renter with ID 1 not found.")));
    }
}
