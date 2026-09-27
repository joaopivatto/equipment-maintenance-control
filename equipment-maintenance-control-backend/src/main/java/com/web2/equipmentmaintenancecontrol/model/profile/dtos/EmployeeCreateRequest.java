package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EmployeeCreateRequest(
    @NotBlank @Email String email,
    @NotBlank String name,
    @NotNull @Past LocalDate birthDate,
    @NotBlank @Size(min = 4, max = 4) String password) {}
