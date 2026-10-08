package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMaintenance(
    @NotBlank(message = "A descrição da manutenção é obrigatória")
        @Size(max = 255, message = "A descrição da manutenção deve ter no máximo 255 caracteres")
        String description,
    @NotBlank(message = "As orientações ao cliente são obrigatórias")
        @Size(max = 255, message = "As orientações ao cliente devem ter no máximo 255 caracteres")
        String customerInstructions,
    @NotNull(message = "O ID do funcionário é obrigatório") Integer employeeId) {}
