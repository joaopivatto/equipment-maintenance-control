package com.web2.equipmentmaintenancecontrol.model.equipment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EquipmentTypeResponse(Integer id, String description) {}
