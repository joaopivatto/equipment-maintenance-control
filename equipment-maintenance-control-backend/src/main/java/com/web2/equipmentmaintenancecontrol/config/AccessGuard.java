package com.web2.equipmentmaintenancecontrol.config;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import java.util.Objects;

/** Utilitário com verificações de acesso reutilizadas pelos controllers. */
public final class AccessGuard {

  private AccessGuard() {}

  /**
   * Garante que o usuário autenticado só acesse os próprios recursos.
   *
   * @param authenticatedUserId id extraído do token JWT
   * @param requestedUserId id do recurso solicitado
   */
  public static void requireSameUser(Integer authenticatedUserId, Integer requestedUserId) {
    if (!Objects.equals(authenticatedUserId, requestedUserId)) {
      throw new AppException(ErrorCode.ACCESS_DENIED);
    }
  }
}
