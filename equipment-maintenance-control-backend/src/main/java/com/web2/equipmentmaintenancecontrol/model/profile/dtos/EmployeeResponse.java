package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeResponse(Integer id, String name, String email, LocalDate birthDate) {}
