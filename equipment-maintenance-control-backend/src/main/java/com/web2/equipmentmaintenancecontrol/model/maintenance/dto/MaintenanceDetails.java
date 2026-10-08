package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MaintenanceDetails(
    Integer id, String description, String instructions, EmployeeResponse employee) {}
