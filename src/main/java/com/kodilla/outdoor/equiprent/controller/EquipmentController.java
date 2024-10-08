package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.exception.ActiveEquipmentRentalException;
import com.kodilla.outdoor.equiprent.exception.CategoryNotFoundException;
import com.kodilla.outdoor.equiprent.exception.EquipmentNotFoundException;
import com.kodilla.outdoor.equiprent.exception.GlobalHttpErrorHandler;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentDto;
import com.kodilla.outdoor.equiprent.facade.EquipmentFacade;
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
@RequestMapping("/api/equipment")
@Tag(name = "Equipment", description = "Managing equipment")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentFacade equipmentFacade;

    @Operation(
            description = "Retrieves all available equipment for rent, with optional filtering by category",
            summary = "Get all available equipment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Equipment retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EquipmentDto.class))
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Category {category} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EquipmentDto>> getAllAvailableEquipment(
            @Parameter(description = "Category of the equipment", example = "TENT")
            @RequestParam Optional<String> category) throws CategoryNotFoundException {
        return ResponseEntity.ok(equipmentFacade.getAllAvailableEquipment(category));
    }

    @Operation(
            summary = "Get equipment details by ID",
            description = "Retrieves details of specific equipment by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Equipment retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EquipmentDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Equipment with ID {equipmentId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @GetMapping(value = "/{equipmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EquipmentDto> getEquipmentById(
            @Parameter(description = "ID of the equipment to retrieve", required = true, example = "1")
            @PathVariable Long equipmentId) throws EquipmentNotFoundException {
        return ResponseEntity.ok(equipmentFacade.getEquipmentById(equipmentId));
    }

    @Operation(
            description = "Retrieves all available equipment categories",
            summary = "Get all available equipment categories"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Categories retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EquipmentCategory.class))
                    ))
    })
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EquipmentCategory>> getAllCategories() {
        return ResponseEntity.ok(equipmentFacade.getAllCategories());
    }

    @Operation(
            description = "Assign equipment to an existing category",
            summary = "Add equipment to category"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Equipment added to category successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EquipmentDto.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Category {category} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @PostMapping(value = "/categories/{category}/equipment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EquipmentDto> createEquipmentInCategory(
            @Parameter(description = "Category of the equipment", required = true, example = "TENT")
            @PathVariable String category,
            @RequestBody CreateEquipmentDto createEquipmentDto) throws CategoryNotFoundException {
        return ResponseEntity.ok(equipmentFacade.createEquipmentInCategory(category, createEquipmentDto));
    }

    @Operation(
            description = "Removes equipment from a specified category",
            summary = "Delete equipment by equipment ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Equipment deleted successfully."),
            @ApiResponse(responseCode = "400",
                    description = "Category {category} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Equipment with ID {equipmentId} not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @DeleteMapping(value = "/categories/{category}/equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEquipmentFromCategory(
            @Parameter(description = "Category of the equipment", required = true, example = "TENT")
            @PathVariable String category,
            @Parameter(description = "ID of the equipment to be deleted", required = true, example = "1")
            @PathVariable Long equipmentId) throws CategoryNotFoundException, EquipmentNotFoundException, ActiveEquipmentRentalException {
        equipmentFacade.deleteEquipmentFromCategory(category, equipmentId);

        return ResponseEntity.noContent().build();
    }
}
