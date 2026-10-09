package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(ProfileType profileType, Integer userId) {}
