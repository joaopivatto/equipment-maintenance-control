package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "O e-mail informado é inválido")
        String email,
    @NotBlank(message = "O nome é obrigatório") String name,
    @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser anterior a hoje")
        LocalDate birthDate) {}
