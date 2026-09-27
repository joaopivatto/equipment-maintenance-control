package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RejectMaintenanceRequest(
        @NotBlank(message = "O motivo da rejeição é obrigatório")
        @Size(max = 500, message = "O motivo da rejeição deve ter no máximo 500 caracteres")
        String reason) {}