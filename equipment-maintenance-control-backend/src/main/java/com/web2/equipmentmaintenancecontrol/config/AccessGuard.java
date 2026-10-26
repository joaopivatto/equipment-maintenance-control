package com.web2.equipmentmaintenancecontrol.config;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import java.util.Objects;

public final class AccessGuard {

  private AccessGuard() {}

  public static void requireSameUser(Integer authenticatedUserId, Integer requestedUserId) {
    if (!Objects.equals(authenticatedUserId, requestedUserId)) {
      throw new AppException(ErrorCode.ACCESS_DENIED);
    }
  }
}
