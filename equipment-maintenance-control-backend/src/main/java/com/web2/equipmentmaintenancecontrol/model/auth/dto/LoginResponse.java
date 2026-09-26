package com.web2.equipmentmaintenancecontrol.model.auth.dto;

import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;

public record LoginResponse(ProfileType profileType, Integer userId) {}
