package com.kodilla.outdoor.equiprent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateRenterDto {
    @Schema(description = "First name of the renter", example = "Bubuslaw", required = true)
    private String firstName;
    @Schema(description = "Last name of the renter", example = "Bubuslawski", required = true)
    private String lastName;
    @Schema(description = "Email address of the renter", example = "bubuslaw@bubuslawski.com", required = true)
    private String email;
    @Schema(description = "Phone number of the renter", example = "123456789", required = true)
    private String phoneNumber;
    @Schema(description = "Home address of the renter", example = "Bubuslawska 1")
    private String address;
}
