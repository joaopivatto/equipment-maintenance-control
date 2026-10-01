package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerformMaintenanceRequest(
        @NotNull(message = "O ID do funcionário é obrigatório") //
        Integer employeeId, //

        @NotBlank(message = "A descrição da manutenção é obrigatória") //
        String description, //

        @NotBlank(message = "As orientações para o cliente são obrigatórias") //
        String customerInstructions //
) {
}