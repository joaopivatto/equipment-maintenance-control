package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RejectMaintenanceRequest(
        @NotNull(message = "O Id do cliente não pode ser nulo") //
        Long customerId, //

        @NotBlank(message = "Motivo da rejeição não pode ser vazio") //
        @Size(max = 500, message = "Motivo da rejeição não pode exceder 500 caracteres") //
        String reason) {
}