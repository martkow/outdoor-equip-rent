package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.dto.CreateRentalDto;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import com.kodilla.outdoor.equiprent.exception.*;
import com.kodilla.outdoor.equiprent.mapper.FilterMapper;
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

import java.io.IOException;
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
    public ResponseEntity<RentalDto> createRental(
            @RequestBody CreateRentalDto createRentalDto,
            @Parameter(description = "Currency code (PLN or EUR)", example = "PLN")
            @RequestParam(defaultValue = "PLN") Optional<String> currencyCode) throws EquipmentNotFoundException, EquipmentNotAvailableException, TierNotAvailableException, RenterNotFoundException, ExchangeRateNotAvailableException, CurrencyCodeNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                rentalMapper.mapRentalToRentalDto(
                        rentalService.createRental(createRentalDto, filterMapper.mapToCurrencyCodeOrThrow(currencyCode))));
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
            description = "Retrieves a specific rental by its ID",
            summary = "Get a rental"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Rental retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Rental with ID {rentalId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
    })
    @GetMapping("/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RentalDto> getRentalById(
            @Parameter(description = "ID of the rental", example = "1")
            @PathVariable Long rentalId) throws RentalNotFoundException {
        return ResponseEntity.ok(rentalMapper.mapRentalToRentalDto(
                rentalService.getRentalById(rentalId)
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

    @Operation(
            description = "Download invoice for a rental in PDF format",
            summary = "Download rental invoice"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice downloaded successfully",
                    content = @Content(
                            mediaType = "application/pdf"
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Rental with ID {rentalId} not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )
            )
    })
    @GetMapping(value = "/{rentalId}/invoice", produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> downloadInvoiceForRental(
            @Parameter(description = "ID of the rental", example = "1")
            @PathVariable Long rentalId) {
        try {
            byte[] pdfInvoice = rentalService.generateInvoiceForRental(rentalId);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=invoice_" + rentalId + ".pdf")
                    .body(pdfInvoice);
        } catch (Exception e) {
            if (e instanceof RentalNotFoundException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new GlobalHttpErrorHandler.Error("ERROR", "rental.does.not.exist", "Rental with ID " + ((RentalNotFoundException) e).getRentalId() + " not found."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new GlobalHttpErrorHandler.Error("ERROR", "invoice.download.not.available", "Invoice for rental with ID " + ((InvoiceDownloadNotAvailableException) e).getRentalId() + " currently not available to download."));

            }
        }
    }
}