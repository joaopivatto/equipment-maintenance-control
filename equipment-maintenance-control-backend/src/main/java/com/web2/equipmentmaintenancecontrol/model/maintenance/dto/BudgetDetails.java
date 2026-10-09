package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BudgetDetails(
    Integer id, BigDecimal value, String createdAt, EmployeeResponse employee) {}
