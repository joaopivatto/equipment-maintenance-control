package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RejectMaintenanceRequest(
    @NotNull(message = "O ID do cliente é obrigatório") Integer customerId,
    @NotBlank(message = "O motivo da rejeição é obrigatório")
        @Size(max = 255, message = "O motivo da rejeição deve ter no máximo 255 caracteres")
        String reason) {}
