package com.web2.equipmentmaintenancecontrol.model.maintenance.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateBudgetDTO(
    @NotNull(message = "O valor do orçamento é obrigatório")
        @Positive(message = "O valor do orçamento deve ser maior que zero")
        BigDecimal value,
    Long employeeId) {}
