package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@Email String email, @NotBlank String password) {}
