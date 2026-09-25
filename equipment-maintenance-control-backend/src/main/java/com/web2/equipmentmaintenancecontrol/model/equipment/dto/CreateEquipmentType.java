package com.web2.equipmentmaintenancecontrol.model.equipment.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateEquipmentType(@NotBlank String description) {}
