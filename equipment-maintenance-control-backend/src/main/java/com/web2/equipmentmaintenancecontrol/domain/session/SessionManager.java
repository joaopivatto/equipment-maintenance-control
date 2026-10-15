package com.web2.equipmentmaintenancecontrol.domain.session;

import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;

public interface SessionManager {
  String generateToken(Integer userId, ProfileType profileType);

  void validateToken(String token);

  Integer extractUserId(String token);

  ProfileType extractProfileType(String token);
}
