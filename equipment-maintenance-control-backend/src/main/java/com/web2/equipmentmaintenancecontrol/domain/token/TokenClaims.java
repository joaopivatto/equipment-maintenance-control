package com.web2.equipmentmaintenancecontrol.domain.token;

import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;

public record TokenClaims(Integer userId, ProfileType profileType) {}
