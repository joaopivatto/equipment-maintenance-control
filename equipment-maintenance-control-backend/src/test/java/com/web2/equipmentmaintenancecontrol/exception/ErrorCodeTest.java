package com.web2.equipmentmaintenancecontrol.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("ErrorCode e AppException")
class ErrorCodeTest {

  @ParameterizedTest(name = "{0} tem status e mensagem definidos")
  @EnumSource(ErrorCode.class)
  void everyCodeHasStatusAndMessage(ErrorCode errorCode) {
    assertThat(errorCode.getStatus()).isNotNull();
    assertThat(errorCode.getMessage()).isNotBlank();
  }

  @Test
  @DisplayName("mapeia os códigos de negócio para os status HTTP esperados")
  void mapsBusinessCodesToHttpStatuses() {
    assertThat(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND.getStatus().value()).isEqualTo(404);
    assertThat(ErrorCode.INVALID_CREDENTIALS.getStatus().value()).isEqualTo(401);
    assertThat(ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED.getStatus().value()).isEqualTo(403);
    assertThat(ErrorCode.INVALID_STATUS_TRANSITION.getStatus().value()).isEqualTo(409);
    assertThat(ErrorCode.VALIDATION_ERROR.getStatus().value()).isEqualTo(400);
    assertThat(ErrorCode.INTERNAL_ERROR.getStatus().value()).isEqualTo(500);
  }

  @Test
  @DisplayName("AppException usa a mensagem do código quando não há detalhe")
  void usesCodeMessageByDefault() {
    AppException exception = new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMPLOYEE_NOT_FOUND);
    assertThat(exception).hasMessage("Employee not found");
  }

  @Test
  @DisplayName("AppException preserva o detalhe e a causa informados")
  void keepsDetailAndCause() {
    RuntimeException cause = new RuntimeException("raiz");
    AppException exception =
        new AppException(ErrorCode.INTERNAL_ERROR, "falha ao gerar hash", cause);

    assertThat(exception).hasMessage("falha ao gerar hash").hasCause(cause);
    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INTERNAL_ERROR);
  }
}
