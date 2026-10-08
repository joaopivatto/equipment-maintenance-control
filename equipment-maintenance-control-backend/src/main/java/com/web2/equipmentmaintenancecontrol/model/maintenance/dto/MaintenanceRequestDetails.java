package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import java.util.List;

public record MaintenanceRequestDetails(
    Integer id,
    String createdAt,
    String updatedAt,
    Integer equipmentId,
    String equipmentDescription,
    Integer equipmentCategoryId,
    String equipmentCategoryName,
    String defectDescription,
    MaintenanceRequestStatus status,
    BudgetDetails budget,
    String rejectionReason,
    Integer customerId,
    String customerName,
    String customerEmail,
    Integer assignedEmployeeId,
    String assignedEmployeeName,
    MaintenanceDetails maintenance,
    String paidAt,
    String finalizedAt,
    String finalizedByEmployeeName,
    List<HistoryEntryDetails> history) {}
