package com.web2.equipmentmaintenancecontrol.model.maintenance.dto;

public record MaintenanceDetails(
    Integer id, String description, String instructions, Integer employeeId, String employeeName) {}
