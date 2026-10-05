package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
    @NotBlank @Pattern(regexp = "\\d{10,11}") String phoneNumber,
    @NotNull @Valid AddressSignupRequest address) {}
