package com.web2.equipmentmaintenancecontrol.model.equipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipmentTypeRequest(
    @NotBlank(message = "A descrição da categoria é obrigatória")
        @Size(max = 255, message = "A descrição da categoria deve ter no máximo 255 caracteres")
        String description) {}
