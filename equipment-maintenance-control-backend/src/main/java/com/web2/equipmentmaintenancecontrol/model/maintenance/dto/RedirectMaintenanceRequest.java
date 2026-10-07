package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotNull;

public record RedirectMaintenanceRequest(
    @NotNull(message = "O ID do funcionário de origem é obrigatório") Integer sourceEmployeeId,
    @NotNull(message = "O ID do funcionário de destino é obrigatório")
        Integer destinationEmployeeId) {}
