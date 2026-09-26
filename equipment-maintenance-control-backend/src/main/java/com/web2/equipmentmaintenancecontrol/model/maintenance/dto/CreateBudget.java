package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateBudget(
    @NotNull(message = "O valor do orçamento é obrigatório")
        @Positive(message = "O valor do orçamento deve ser maior que zero")
        BigDecimal value,
    Long employeeId) {}
