package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentResponse;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.CustomerResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MaintenanceRequestDetails(
    Integer id,
    String createdAt,
    String updatedAt,
    EquipmentResponse equipment,
    String defectDescription,
    MaintenanceRequestStatus status,
    BudgetDetails budget,
    String rejectionReason,
    CustomerResponse customer,
    EmployeeResponse assignedEmployee,
    MaintenanceDetails maintenance,
    String paidAt,
    String finalizedAt,
    EmployeeResponse finalizedBy,
    List<HistoryEntryDetails> history) {}
