package com.web2.equipmentmaintenancecontrol.model.maintenance;

import java.util.Map;
import java.util.Set;

/** Estados possíveis de uma solicitação de manutenção, na ordem do fluxo principal. */
public enum MaintenanceRequestStatus {
  ABERTA,
  ORCADA,
  REJEITADA,
  APROVADA,
  REDIRECIONADA,
  ARRUMADA,
  PAGA,
  FINALIZADA;

  /** Mapa de transições válidas: para cada estado, os estados que podem vir em seguida. */
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

  /** Indica se é permitido mudar do estado atual para o estado de destino. */
  public boolean canTransitionTo(MaintenanceRequestStatus target) {
    return target != null && allowedTransitions().contains(target);
  }
}
