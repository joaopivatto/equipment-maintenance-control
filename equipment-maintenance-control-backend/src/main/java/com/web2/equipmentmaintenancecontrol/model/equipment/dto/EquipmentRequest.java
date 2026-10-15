package com.web2.equipmentmaintenancecontrol.model.equipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipmentRequest(
    @NotBlank(message = "A descrição do equipamento é obrigatória")
        @Size(max = 255, message = "A descrição do equipamento deve ter no máximo 255 caracteres")
        String description,
    @NotNull(message = "O ID da categoria do equipamento é obrigatório") Integer typeId) {}
