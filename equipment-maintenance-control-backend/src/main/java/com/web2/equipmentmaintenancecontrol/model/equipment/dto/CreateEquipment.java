package com.web2.equipmentmaintenancecontrol.model.equipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEquipment(@NotBlank String description, @NotNull Integer typeId) {}
