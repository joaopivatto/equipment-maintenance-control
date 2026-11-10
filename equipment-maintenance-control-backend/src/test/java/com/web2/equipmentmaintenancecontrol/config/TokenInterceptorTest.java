package com.web2.equipmentmaintenancecontrol.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.domain.token.TokenClaims;
import com.web2.equipmentmaintenancecontrol.domain.token.TokenManager;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenInterceptor")
class TokenInterceptorTest {

  private static final String TOKEN = "token-valido";

  @Mock private TokenManager tokenManager;

  private TokenInterceptor interceptor;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    interceptor = new TokenInterceptor(tokenManager);
    response = new MockHttpServletResponse();
  }

  @Nested
  @DisplayName("requisição de preflight")
  class Preflight {

    @Test
    @DisplayName("libera OPTIONS sem validar token")
    void allowsOptions() {
      MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/equipment");

      boolean allowed = interceptor.preHandle(request, response, new Object());

      assertThat(allowed).isTrue();
      assertThat(request.getAttribute(TokenInterceptor.USER_ID_ATTRIBUTE)).isNull();
      verifyNoInteractions(tokenManager);
    }
  }

  @Nested
  @DisplayName("extração do token")
  class TokenExtraction {

    @Test
    @DisplayName("repassa o token sem o prefixo Bearer ao TokenManager")
    void stripsBearerPrefix() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(7, ProfileType.CUSTOMER));

      boolean allowed = interceptor.preHandle(get("Bearer " + TOKEN), response, new Object());

      assertThat(allowed).isTrue();
    }

    @Test
    @DisplayName("recusa requisição sem header Authorization")
    void rejectsMissingHeader() {
      assertThatThrownBy(
              () ->
                  interceptor.preHandle(
                      new MockHttpServletRequest("GET", "/equipment"), response, new Object()))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_OR_EXPIRED_TOKEN);

      verifyNoInteractions(tokenManager);
    }

    @ParameterizedTest(name = "header \"{0}\"")
    @ValueSource(strings = {"", "token-valido", "bearer token-valido", "Basic token-valido"})
    @DisplayName("recusa header sem o prefixo Bearer")
    void rejectsHeaderWithoutBearerPrefix(String header) {
      assertThatThrownBy(() -> interceptor.preHandle(get(header), response, new Object()))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_OR_EXPIRED_TOKEN);

      verifyNoInteractions(tokenManager);
    }

    @Test
    @DisplayName("propaga erro do TokenManager para token inválido")
    void propagatesTokenManagerError() {
      when(tokenManager.parseToken(TOKEN))
          .thenThrow(new AppException(ErrorCode.INVALID_OR_EXPIRED_TOKEN));

      assertThatThrownBy(
              () -> interceptor.preHandle(get("Bearer " + TOKEN), response, new Object()))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
    }
  }

  @Nested
  @DisplayName("atributos da requisição")
  class RequestAttributes {

    @ParameterizedTest(name = "{1} de id {0}")
    @CsvSource({"3,CUSTOMER", "9,EMPLOYEE"})
    @DisplayName("publica userId e profileType na requisição")
    void exposesClaims(Integer userId, ProfileType profileType) {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(userId, profileType));
      MockHttpServletRequest request = get("Bearer " + TOKEN);

      boolean allowed = interceptor.preHandle(request, response, new Object());

      assertThat(allowed).isTrue();
      assertThat(request.getAttribute(TokenInterceptor.USER_ID_ATTRIBUTE)).isEqualTo(userId);
      assertThat(request.getAttribute(TokenInterceptor.PROFILE_TYPE_ATTRIBUTE))
          .isEqualTo(profileType);
    }
  }

  @Nested
  @DisplayName("restrição por perfil")
  class ProfileRestriction {

    @Test
    @DisplayName("permite handler sem anotação para qualquer perfil")
    void allowsUnannotatedHandler() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(1, ProfileType.EMPLOYEE));

      boolean allowed = interceptor.preHandle(get("Bearer " + TOKEN), response, handler("open"));

      assertThat(allowed).isTrue();
    }

    @Test
    @DisplayName("permite perfil exigido pelo método")
    void allowsProfileRequiredByMethod() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(1, ProfileType.CUSTOMER));

      boolean allowed =
          interceptor.preHandle(get("Bearer " + TOKEN), response, handler("customerOnly"));

      assertThat(allowed).isTrue();
    }

    @Test
    @DisplayName("recusa perfil diferente do exigido pelo método")
    void deniesProfileNotRequiredByMethod() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(1, ProfileType.EMPLOYEE));

      assertThatThrownBy(
              () ->
                  interceptor.preHandle(get("Bearer " + TOKEN), response, handler("customerOnly")))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.ACCESS_DENIED);
    }

    @Test
    @DisplayName("herda exigência declarada na classe do controller")
    void inheritsRequirementFromBeanType() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(1, ProfileType.CUSTOMER));

      assertThatThrownBy(
              () ->
                  interceptor.preHandle(
                      get("Bearer " + TOKEN), response, handler(EmployeeController.class, "list")))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.ACCESS_DENIED);
    }

    @Test
    @DisplayName("anotação do método prevalece sobre a da classe")
    void methodAnnotationOverridesBeanType() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(1, ProfileType.CUSTOMER));

      boolean allowed =
          interceptor.preHandle(
              get("Bearer " + TOKEN), response, handler(EmployeeController.class, "customerOnly"));

      assertThat(allowed).isTrue();
    }

    @Test
    @DisplayName("não aplica restrição quando o handler não é HandlerMethod")
    void ignoresNonHandlerMethod() {
      when(tokenManager.parseToken(TOKEN)).thenReturn(new TokenClaims(1, ProfileType.EMPLOYEE));

      boolean allowed = interceptor.preHandle(get("Bearer " + TOKEN), response, "recurso-estatico");

      assertThat(allowed).isTrue();
    }
  }

  private static MockHttpServletRequest get(String authorizationHeader) {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/equipment");
    request.addHeader("Authorization", authorizationHeader);

    return request;
  }

  private static HandlerMethod handler(String methodName) {
    return handler(OpenController.class, methodName);
  }

  private static HandlerMethod handler(Class<?> controller, String methodName) {
    try {
      Method method = controller.getDeclaredMethod(methodName);

      return new HandlerMethod(controller.getDeclaredConstructor().newInstance(), method);
    } catch (ReflectiveOperationException exception) {
      throw new IllegalStateException(exception);
    }
  }

  static class OpenController {

    public void open() {}

    @RequiresProfile(ProfileType.CUSTOMER)
    public void customerOnly() {}
  }

  @RequiresProfile(ProfileType.EMPLOYEE)
  static class EmployeeController {

    public void list() {}

    @RequiresProfile(ProfileType.CUSTOMER)
    public void customerOnly() {}
  }
}
