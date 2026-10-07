package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotNull;

public record ApproveMaintenanceRequest(
    @NotNull(message = "O ID do cliente é obrigatório") Integer customerId) {}
