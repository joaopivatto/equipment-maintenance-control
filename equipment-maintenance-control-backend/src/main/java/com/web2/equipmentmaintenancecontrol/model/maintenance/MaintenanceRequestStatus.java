package com.web2.equipmentmaintenancecontrol.model.maintenance;

import java.util.Map;
import java.util.Set;

public enum MaintenanceRequestStatus {
  ABERTA,
  ORCADA,
  REJEITADA,
  APROVADA,
  REDIRECIONADA,
  ARRUMADA,
  PAGA,
  FINALIZADA;

  private static final Map<MaintenanceRequestStatus, Set<MaintenanceRequestStatus>>
      ALLOWED_TRANSITIONS =
          Map.of(
              ABERTA, Set.of(ORCADA),
              ORCADA, Set.of(APROVADA, REJEITADA),
              REJEITADA, Set.of(APROVADA),
              APROVADA, Set.of(ARRUMADA, REDIRECIONADA),
              REDIRECIONADA, Set.of(ARRUMADA, REDIRECIONADA),
              ARRUMADA, Set.of(PAGA),
              PAGA, Set.of(FINALIZADA),
              FINALIZADA, Set.of());

  public Set<MaintenanceRequestStatus> allowedTransitions() {
    return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of());
  }

  public boolean canTransitionTo(MaintenanceRequestStatus target) {
    return target != null && allowedTransitions().contains(target);
  }
}
