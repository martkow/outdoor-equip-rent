package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.exception.GlobalHttpErrorHandler;
import com.kodilla.outdoor.equiprent.exception.RentalNotFoundException;
import com.kodilla.outdoor.equiprent.exception.RenterAlreadyExistsException;
import com.kodilla.outdoor.equiprent.exception.RenterNotFoundException;
import com.kodilla.outdoor.equiprent.dto.CreateUpdateRenterDto;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import com.kodilla.outdoor.equiprent.dto.RenterDto;
import com.kodilla.outdoor.equiprent.mapper.RentalMapper;
import com.kodilla.outdoor.equiprent.mapper.RenterMapper;
import com.kodilla.outdoor.equiprent.service.RentalService;
import com.kodilla.outdoor.equiprent.service.RenterService;
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

@RestController
@RequestMapping("/api/renters")
@Tag(name = "Renter", description = "Managing renters")
@RequiredArgsConstructor
public class RenterController {
    private final RenterService renterService;
    private final RenterMapper renterMapper;
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @Operation(summary = "Create a new renter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Renter created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RenterDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Renter with email {email} already exists.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RenterDto> createRenter(@RequestBody CreateUpdateRenterDto createUpdateRenterDto) throws RenterAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(renterMapper.mapRenterToRenterDto(renterService.createRenter(renterMapper.mapCreateUpdateRenterDtoToRenter(createUpdateRenterDto))));
    }

    @Operation(
            summary = "Update an existing renter",
            description = "Updates an existing renter's details by renter ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Renter updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RenterDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Renter with ID {renterId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @PutMapping(value = "/{renterId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateRenter(
            @Parameter(description = "ID of the renter to update", example = "1")
            @PathVariable Long renterId,
            @RequestBody CreateUpdateRenterDto createUpdateRenterDto) throws RenterNotFoundException {
        renterService.updateRenter(renterId, renterMapper.mapCreateUpdateRenterDtoToRenter(createUpdateRenterDto));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            description = "Retrieves all renters",
            summary = "Get all renters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of renters retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RenterDto.class))
                    ))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RenterDto>> getAllRenters() {
        return ResponseEntity.ok(renterMapper.mapRenterListToRenterDtoList(renterService.getAllRenters()));
    }

    @Operation(
            description = "Retrieves a specific renter by its ID",
            summary = "Get renter by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Renter retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RenterDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Renter with ID {renterId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
    })
    @GetMapping(value = "/{renterId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RenterDto> getRenter(
            @Parameter(description = "ID of the renter", example = "1")
            @PathVariable Long renterId) throws RenterNotFoundException {
        return ResponseEntity.ok(renterMapper.mapRenterToRenterDto(renterService.getRenter(renterId)));
    }

    @Operation(
            description = "Get all rentals of a renter by renter ID",
            summary = "Get all rentals"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Renter retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RentalDto.class))
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Renter with ID {renterId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
    })
    @GetMapping("/{renterId}/rentals")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RentalDto>> getRentalsByRenterId(
            @Parameter(description = "ID of the renter", example = "1")
            @PathVariable Long renterId) throws RenterNotFoundException {
        return ResponseEntity.ok(rentalMapper.mapRentalListToRentalDtoList(rentalService.getRentalsByRenterId(renterId)));
    }

    @Operation(
            description = "Get a specific rental by renter ID and rental ID",
            summary = "Get rental by renter and rental ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Rental retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Renter with ID {renterId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Rental with ID {rentalId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
    })
    @GetMapping("/{renterId}/rentals/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RentalDto> getRentalByRenterIdAndRentalId(
            @Parameter(description = "ID of the renter", example = "1")
            @PathVariable Long renterId,
            @Parameter(description = "ID of the rental", example = "1")
            @PathVariable Long rentalId) throws RenterNotFoundException, RentalNotFoundException {
        return ResponseEntity.ok(rentalMapper.mapRentalToRentalDto(rentalService.getRentalByRenterIdAndRentalId(renterId, rentalId)));
    }
}