package com.web2.equipmentmaintenancecontrol.model.equipment.dtos;

import jakarta.validation.constraints.NotBlank;

public record EquipmentTypeRequest(@NotBlank String description) {}
