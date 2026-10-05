package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @Email String email, @NotBlank @Size(min = 4, max = 4) String password) {}
