package com.web2.equipmentmaintenancecontrol.domain.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("JwtTokenManager")
class JwtTokenManagerTest {

  private static final String SECRET =
      "dGVzdC1zZWNyZXQtbmFvLXVzYXItZW0tcHJvZHVjYW8tMTIzNDU2Nzg5MA==";
  private static final String OTHER_SECRET =
      "b3V0cm8tc2VjcmV0by1xdWFscXVlci1wYXJhLXRlc3RlLTEyMzQ1Njc4OTA=";

  private JwtTokenManager tokenManager;

  @BeforeEach
  void setUp() {
    tokenManager = newTokenManager(SECRET, 86_400_000L);
  }

  @Test
  @DisplayName("gera token com subject e claim de tipo de perfil")
  void generatesTokenWithClaims() {
    String token = tokenManager.generateToken(42, ProfileType.EMPLOYEE);

    assertThat(token).isNotBlank().contains(".");

    TokenClaims claims = tokenManager.parseToken(token);

    assertThat(claims.userId()).isEqualTo(42);
    assertThat(claims.profileType()).isEqualTo(ProfileType.EMPLOYEE);
  }

  @Test
  @DisplayName("lê token recém-gerado")
  void parsesFreshToken() {
    String token = tokenManager.generateToken(1, ProfileType.CUSTOMER);

    assertThatCode(() -> tokenManager.parseToken(token)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("rejeita token malformado")
  void rejectsMalformedToken() {
    assertThatThrownBy(() -> tokenManager.parseToken("nao-e-um-jwt"))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
  }

  @Test
  @DisplayName("rejeita token assinado com outro segredo")
  void rejectsTokenSignedWithAnotherSecret() {
    String foreignToken =
        newTokenManager(OTHER_SECRET, 86_400_000L).generateToken(1, ProfileType.CUSTOMER);

    assertThatThrownBy(() -> tokenManager.parseToken(foreignToken))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
  }

  @Test
  @DisplayName("rejeita token expirado")
  void rejectsExpiredToken() {
    JwtTokenManager expiredManager = newTokenManager(SECRET, -1_000L);
    String token = expiredManager.generateToken(1, ProfileType.CUSTOMER);

    assertThatThrownBy(() -> expiredManager.parseToken(token))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
  }

  private JwtTokenManager newTokenManager(String secret, long expirationMs) {
    JwtTokenManager manager = new JwtTokenManager();
    ReflectionTestUtils.setField(manager, "secret", secret);
    ReflectionTestUtils.setField(manager, "expirationMs", expirationMs);
    return manager;
  }
}
