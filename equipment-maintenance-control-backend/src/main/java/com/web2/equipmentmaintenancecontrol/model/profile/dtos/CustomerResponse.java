package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(Integer id, String name, String email) {}
