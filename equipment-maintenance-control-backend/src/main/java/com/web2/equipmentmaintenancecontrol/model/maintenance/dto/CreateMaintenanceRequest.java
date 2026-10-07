package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMaintenanceRequest(
    @NotNull(message = "O ID do cliente é obrigatório") Integer customerId,
    @NotBlank(message = "A descrição do equipamento é obrigatória")
        @Size(max = 255, message = "A descrição do equipamento deve ter no máximo 255 caracteres")
        String equipmentDescription,
    @NotNull(message = "O ID da categoria do equipamento é obrigatório") Integer equipmentTypeId,
    @NotBlank(message = "A descrição do defeito é obrigatória")
        @Size(max = 255, message = "A descrição do defeito deve ter no máximo 255 caracteres")
        String defectDescription) {}
