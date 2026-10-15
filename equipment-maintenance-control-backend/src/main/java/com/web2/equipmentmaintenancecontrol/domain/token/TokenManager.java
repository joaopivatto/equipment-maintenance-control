package com.web2.equipmentmaintenancecontrol.domain.token;

import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;

public interface TokenManager {
  String generateToken(Integer userId, ProfileType profileType);

  TokenClaims parseToken(String token);
}
