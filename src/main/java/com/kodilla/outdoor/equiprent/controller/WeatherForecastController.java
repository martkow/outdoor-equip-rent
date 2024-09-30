package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.exception.GlobalHttpErrorHandler;
import com.kodilla.outdoor.equiprent.exception.WeatherForecastNotAvailableException;
import com.kodilla.outdoor.equiprent.dto.WeatherMapDto;
import com.kodilla.outdoor.equiprent.external.open.weather.map.client.OpenWeatherMapClient;
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

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather forecast", description = "Fetching weather forecast for any location")
@RequiredArgsConstructor
public class WeatherForecastController {
    private final OpenWeatherMapClient openWeatherMapClient;

    @Operation(
            description = "Retrieves 5 day forecast at any location on the globe. It includes weather forecast data with 3-hour step",
            summary = "Get weather forecast"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Forecast retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = WeatherMapDto.class))
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Weather forecast currently not available.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)
                    ))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WeatherMapDto> getAllAvailableEquipment(
            @Parameter(description = "Latitude of the location", required = true, example = "53.16536")
            @RequestParam double latitude,
            @Parameter(description = "Longitude of the location", required = true, example = "19.39033")
            @RequestParam double longitude) throws WeatherForecastNotAvailableException {
        return ResponseEntity.ok(openWeatherMapClient.getWeatherForecast(latitude, longitude));
    }
}
