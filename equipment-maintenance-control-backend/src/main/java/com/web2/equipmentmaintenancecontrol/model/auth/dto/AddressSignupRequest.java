package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record AddressSignupRequest(
    @NotBlank(message = "A rua é obrigatória") String street,
    @NotNull(message = "O número é obrigatório")
        @Positive(message = "O número deve ser maior que zero")
        Integer number,
    String complement,
    @NotBlank(message = "O bairro é obrigatório") String neighborhood,
    @NotBlank(message = "A cidade é obrigatória") String city,
    @NotBlank(message = "O estado é obrigatório") String state,
    @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve ter 8 dígitos")
        String zipCode) {}
