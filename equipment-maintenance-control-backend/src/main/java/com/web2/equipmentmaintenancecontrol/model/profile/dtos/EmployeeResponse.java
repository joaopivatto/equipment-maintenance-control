package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import java.time.LocalDate;

public record EmployeeResponse(
        Integer id,
        String name,
        String email,
        LocalDate birthDate,
        boolean active
) {}
