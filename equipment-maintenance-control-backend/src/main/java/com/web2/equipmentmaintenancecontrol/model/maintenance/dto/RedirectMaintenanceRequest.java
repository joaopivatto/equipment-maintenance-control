package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotNull;

public record RedirectMaintenanceRequest(
        @NotNull(message = "Funcionário de origem é obrigatório") Integer sourceEmployeeId,
        @NotNull(message = "Funcionário de destino é obrigatório") Integer destinationEmployeeId) {
}