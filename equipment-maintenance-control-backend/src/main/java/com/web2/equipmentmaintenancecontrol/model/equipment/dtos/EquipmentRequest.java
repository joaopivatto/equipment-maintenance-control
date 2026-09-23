package com.web2.equipmentmaintenancecontrol.model.equipment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipmentRequest(@NotBlank String description, @NotNull Integer typeId) {}
