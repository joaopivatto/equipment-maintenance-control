package com.web2.equipmentmaintenancecontrol.model.maintenance;

import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.ABERTA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.APROVADA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.ARRUMADA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.FINALIZADA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.ORCADA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.PAGA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.REDIRECIONADA;
import static com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus.REJEITADA;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("MaintenanceRequestStatus")
class MaintenanceRequestStatusTest {

  @Test
  @DisplayName("declara as transições permitidas de cada status")
  void declaresAllowedTransitions() {
    assertThat(ABERTA.allowedTransitions()).containsExactly(ORCADA);
    assertThat(ORCADA.allowedTransitions()).containsExactlyInAnyOrder(APROVADA, REJEITADA);
    assertThat(REJEITADA.allowedTransitions()).containsExactly(APROVADA);
    assertThat(APROVADA.allowedTransitions()).containsExactlyInAnyOrder(ARRUMADA, REDIRECIONADA);
    assertThat(REDIRECIONADA.allowedTransitions())
        .containsExactlyInAnyOrder(ARRUMADA, REDIRECIONADA);
    assertThat(ARRUMADA.allowedTransitions()).containsExactly(PAGA);
    assertThat(PAGA.allowedTransitions()).containsExactly(FINALIZADA);
    assertThat(FINALIZADA.allowedTransitions()).isEmpty();
  }

  @ParameterizedTest(name = "{0} -> {1} é permitido")
  @CsvSource({
    "ABERTA,ORCADA",
    "ORCADA,APROVADA",
    "ORCADA,REJEITADA",
    "REJEITADA,APROVADA",
    "APROVADA,ARRUMADA",
    "APROVADA,REDIRECIONADA",
    "REDIRECIONADA,ARRUMADA",
    "REDIRECIONADA,REDIRECIONADA",
    "ARRUMADA,PAGA",
    "PAGA,FINALIZADA"
  })
  void allowsValidTransitions(MaintenanceRequestStatus from, MaintenanceRequestStatus to) {
    assertThat(from.canTransitionTo(to)).isTrue();
  }

  @ParameterizedTest(name = "{0} -> {1} é bloqueado")
  @CsvSource({
    "ABERTA,APROVADA",
    "ABERTA,FINALIZADA",
    "ORCADA,ARRUMADA",
    "ORCADA,PAGA",
    "REJEITADA,REJEITADA",
    "APROVADA,PAGA",
    "ARRUMADA,FINALIZADA",
    "PAGA,PAGA",
    "FINALIZADA,ABERTA"
  })
  void blocksInvalidTransitions(MaintenanceRequestStatus from, MaintenanceRequestStatus to) {
    assertThat(from.canTransitionTo(to)).isFalse();
  }

  @ParameterizedTest(name = "{0} nunca transita para null")
  @EnumSource(MaintenanceRequestStatus.class)
  void blocksNullTarget(MaintenanceRequestStatus from) {
    assertThat(from.canTransitionTo(null)).isFalse();
  }

  @ParameterizedTest(name = "{0} não transita para si mesmo (exceto REDIRECIONADA)")
  @EnumSource(
      value = MaintenanceRequestStatus.class,
      names = {"REDIRECIONADA"},
      mode = EnumSource.Mode.EXCLUDE)
  void blocksSelfTransition(MaintenanceRequestStatus status) {
    assertThat(status.canTransitionTo(status)).isFalse();
  }
}
