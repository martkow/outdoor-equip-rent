package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.controller.exception.*;
import com.kodilla.outdoor.equiprent.controller.filter.FilterMapper;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.dto.CreateRentalDto;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import com.kodilla.outdoor.equiprent.mapper.RentalMapper;
import com.kodilla.outdoor.equiprent.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "Managing equipment rentals")
@RequiredArgsConstructor
public class EquipmentRentController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final FilterMapper filterMapper;

    @Operation(
            description = "Creates a new rental",
            summary = "Create a new rental"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Rental created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Equipment with ID {equipmentId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Equipment with ID {equipmentId} not available.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Rental tier with ID {rentalTierId} not available.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RentalDto> createRental(@RequestBody CreateRentalDto createRentalDto) throws EquipmentNotFoundException, EquipmentNotAvailableException, TierNotAvailableException, RenterNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                rentalMapper.mapRentalToRentalDto(rentalService.createRental(
                        createRentalDto.getEquipmentId(),
                        createRentalDto.getRenterId(),
                        createRentalDto.getRentalTierId(),
                        createRentalDto.getRentalTierQuantity())
                ));
    }

    @Operation(
            description = "Retrieves all rentals, with optional filtering by status",
            summary = "Get all rentals"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of rentals retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RentalDto.class))
                    ))
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RentalDto>> getAllRentals(
            @Parameter(description = "Status of the rental", example = "ACTIVE")
            @RequestParam Optional<String> status) throws RentalStatusNotFoundException {
        return ResponseEntity.ok(rentalMapper.mapRentalListToRentalDtoList(
                rentalService.getRentalByStatuses(
                    filterMapper.mapStringToRentalStatusList(status)
                )
        ));
    }

    @Operation(
            description = "Updates rental status",
            summary = "Update rental status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Rental status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Rental with ID {rentalId} not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class))),
            @ApiResponse(responseCode = "400",
                    description = "Rental status {status} not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class))),
            @ApiResponse(responseCode = "400",
                    description = "Rental status change from {nowRentalStatus} to {status} is invalid.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @PatchMapping("/{rentalId}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RentalDto> updateRentalStatus(
            @Parameter(description = "ID of the equipment being rented", example = "1")
            @PathVariable Long rentalId,
            @Parameter(description = "Status of the rental", example = "ACTIVE")
            @RequestParam String status) throws RentalStatusNotFoundException, RentalNotFoundException, InvalidRentalStatusChangeException {
        return ResponseEntity.ok(rentalMapper.mapRentalToRentalDto(
                rentalService.updateRentalStatus(
                        rentalId,
                        filterMapper.mapToStatusOrThrow(status))
        ));
    }
}
