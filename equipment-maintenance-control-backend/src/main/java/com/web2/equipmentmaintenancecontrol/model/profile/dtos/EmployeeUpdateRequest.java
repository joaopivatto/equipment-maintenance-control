package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
    @NotBlank @Email String email, @NotBlank String name, @NotNull @Past LocalDate birthDate) {}
