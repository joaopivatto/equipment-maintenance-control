package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotNull;

public record FinishMaintenanceRequest(
    @NotNull(message = "O ID do funcionário é obrigatório") Integer employeeId) {}
