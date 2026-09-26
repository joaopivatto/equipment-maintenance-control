package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMaintenanceRequest(
    @NotNull(message = "O ID do cliente é obrigatório") Integer customerId,
    @NotNull(message = "O ID do equipamento é obrigatório") Integer equipmentId,
    @NotBlank(message = "A descrição do defeito é obrigatória")
        @Size(max = 255, message = "A descrição do defeito deve ter no máximo 255 caracteres")
        String defectDescription) {}
