package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMaintenanceRequest(
    @NotNull(message = "O ID do cliente é obrigatório") Long customerId,
    @NotNull(message = "O ID do tipo de equipamento é obrigatório") Long equipmentTypeId,
    @NotBlank(message = "A descrição do problema/equipamento é obrigatória")
        String equipmentDescription) {}
