package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;

public record HistoryEntryDetails(
    MaintenanceRequestStatus status,
    String dateTime,
    String responsible,
    String reason,
    String fromEmployee,
    String toEmployee) {}
