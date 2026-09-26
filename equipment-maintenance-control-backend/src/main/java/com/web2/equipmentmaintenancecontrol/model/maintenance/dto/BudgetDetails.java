package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import java.math.BigDecimal;

public record BudgetDetails(Integer id, BigDecimal value, String createdAt, Integer employeeId) {}
