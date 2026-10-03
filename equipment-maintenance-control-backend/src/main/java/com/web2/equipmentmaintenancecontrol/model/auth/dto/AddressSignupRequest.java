package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.constraints.*;

public record AddressSignupRequest(
    @NotBlank String street,
    @NotNull @Positive Integer number,
    String complement,
    @NotBlank String neighborhood,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank @Pattern(regexp = "\\d{8}") String zipCode) {}
