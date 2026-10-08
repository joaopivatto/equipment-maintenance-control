package com.web2.equipmentmaintenancecontrol.model.equipment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EquipmentResponse(Integer id, String description, EquipmentTypeResponse type) {}
