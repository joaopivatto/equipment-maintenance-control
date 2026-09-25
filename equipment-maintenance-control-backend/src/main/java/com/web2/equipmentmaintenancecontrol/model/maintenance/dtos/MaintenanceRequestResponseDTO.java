package com.web2.equipmentmaintenancecontrol.model.maintenance.dtos;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MaintenanceRequestResponseDTO(Long id, LocalDateTime createdAt, String customerName, Long customerId,
        String equipmentDescription, String categoryName, MaintenanceRequestStatus status, BigDecimal budgetValue,
        String justification) {
}
