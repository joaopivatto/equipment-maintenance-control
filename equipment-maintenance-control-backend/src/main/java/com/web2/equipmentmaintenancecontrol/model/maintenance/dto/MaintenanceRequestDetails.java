package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import java.math.BigDecimal;

public record MaintenanceRequestDetails(
    Long id,
    String createdAt,
    String customerName,
    Long customerId,
    String equipmentDescription,
    String categoryName,
    MaintenanceRequestStatus status,
    BigDecimal budgetValue,
    String justification) {}
