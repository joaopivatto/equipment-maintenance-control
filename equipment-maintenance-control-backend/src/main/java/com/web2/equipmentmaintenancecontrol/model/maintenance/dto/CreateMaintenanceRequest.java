package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMaintenanceRequest(
    @NotNull(message = "O ID do cliente é obrigatório") Integer customerId,
    @NotNull(message = "Os dados do equipamento são obrigatórios") @Valid
        EquipmentRequest equipment,
    @NotBlank(message = "A descrição do defeito é obrigatória")
        @Size(max = 255, message = "A descrição do defeito deve ter no máximo 255 caracteres")
        String defectDescription) {}
