package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EmployeeCreateRequest(
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "O e-mail informado é inválido")
        String email,
    @NotBlank(message = "O nome é obrigatório") String name,
    @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser anterior a hoje")
        LocalDate birthDate,
    @NotBlank(message = "A senha é obrigatória")
        @Size(min = 4, max = 4, message = "A senha deve ter 4 caracteres")
        String password) {}
