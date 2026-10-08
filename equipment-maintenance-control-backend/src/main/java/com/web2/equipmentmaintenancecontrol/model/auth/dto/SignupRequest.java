package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record SignupRequest(
    @NotBlank(message = "O nome é obrigatório") String name,
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "O e-mail informado é inválido")
        String email,
    @NotBlank(message = "O CPF é obrigatório") @CPF(message = "O CPF informado é inválido")
        String cpf,
    @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter 10 ou 11 dígitos")
        String phoneNumber,
    @NotNull(message = "O endereço é obrigatório") @Valid AddressSignupRequest address) {}
