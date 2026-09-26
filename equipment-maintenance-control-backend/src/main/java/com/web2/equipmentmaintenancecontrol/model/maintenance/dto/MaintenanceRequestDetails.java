package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import java.util.List;

public record MaintenanceRequestDetails(
    Integer id,
    String createdAt,
    String equipmentDescription,
    String equipmentCategoryName,
    String defectDescription,
    MaintenanceRequestStatus status,
    BudgetDetails budget,
    String rejectionReason,
    String customerName,
    MaintenanceDetails maintenance,
    String paidAt,
    String finalizedAt,
    String finalizedByEmployeeName,
    List<HistoryEntryDetails> history) {}
