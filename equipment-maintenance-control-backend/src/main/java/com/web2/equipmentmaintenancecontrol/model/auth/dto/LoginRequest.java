package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "O e-mail informado é inválido")
        String email,
    @NotBlank(message = "A senha é obrigatória")
        @Size(min = 4, max = 4, message = "A senha deve ter 4 caracteres")
        String password) {}
