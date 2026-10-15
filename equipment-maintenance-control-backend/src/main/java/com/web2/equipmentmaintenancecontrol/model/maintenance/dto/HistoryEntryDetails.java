package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HistoryEntryDetails(
    MaintenanceRequestStatus status,
    String dateTime,
    EmployeeResponse responsible,
    String reason,
    EmployeeResponse fromEmployee,
    EmployeeResponse toEmployee) {}
