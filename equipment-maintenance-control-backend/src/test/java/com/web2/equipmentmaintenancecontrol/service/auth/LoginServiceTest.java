package com.web2.equipmentmaintenancecontrol.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.domain.token.TokenManager;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.LoginResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.repository.profile.ProfileRepository;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService")
class LoginServiceTest {

  @Mock private ProfileRepository repository;
  @Mock private PasswordHasherSalter passwordHasherSalter;
  @Mock private TokenManager tokenManager;

  private LoginService service;

  @BeforeEach
  void setUp() {
    service = new LoginService(repository, passwordHasherSalter, tokenManager);
  }

  @Test
  @DisplayName("autentica cliente e devolve dados do perfil")
  void authenticatesCustomer() {
    Customer customer = Fixtures.customer(5, "Bruno");
    when(repository.findByEmail("cliente5@web2.com")).thenReturn(customer);
    when(passwordHasherSalter.matches(eq("1234"), any(HashedSaltedPassword.class)))
            .thenReturn(true);
    when(tokenManager.generateToken(5, ProfileType.CUSTOMER)).thenReturn("token-cliente");

    LoginResponse response = service.execute(new LoginRequest("cliente5@web2.com", "1234"));

    assertThat(response.token()).isEqualTo("token-cliente");

    assertThat(response.userId()).isEqualTo(5);
    assertThat(response.name()).isEqualTo("Bruno");
    assertThat(response.email()).isEqualTo("cliente5@web2.com");
    assertThat(response.profileType()).isEqualTo(ProfileType.CUSTOMER);
  }

  @Test
  @DisplayName("autentica funcionário e devolve o tipo EMPLOYEE")
  void authenticatesEmployee() {
    Employee employee = Fixtures.employee(9, "Ana");
    when(repository.findByEmail(employee.getEmail())).thenReturn(employee);
    when(passwordHasherSalter.matches(eq("1234"), any(HashedSaltedPassword.class)))
            .thenReturn(true);
    when(tokenManager.generateToken(9, ProfileType.EMPLOYEE)).thenReturn("token-funcionario");

    LoginResponse response = service.execute(new LoginRequest(employee.getEmail(), "1234"));

    assertThat(response.token()).isEqualTo("token-funcionario");

    assertThat(response.profileType()).isEqualTo(ProfileType.EMPLOYEE);
    assertThat(response.userId()).isEqualTo(9);
  }

  @Test
  @DisplayName("confere a senha contra o hash e o salt armazenados")
  void checksPasswordAgainstStoredHash() {
    Customer customer = Fixtures.customer(5);
    when(repository.findByEmail(customer.getEmail())).thenReturn(customer);
    when(passwordHasherSalter.matches(any(), any())).thenReturn(true);

    service.execute(new LoginRequest(customer.getEmail(), "1234"));

    ArgumentCaptor<HashedSaltedPassword> captor =
            ArgumentCaptor.forClass(HashedSaltedPassword.class);
    verify(passwordHasherSalter).matches(eq("1234"), captor.capture());
    assertThat(captor.getValue().hash()).isEqualTo(customer.getPasswordHash());
    assertThat(captor.getValue().salt()).isEqualTo(customer.getPasswordSalt());
  }

  @Test
  @DisplayName("falha com INVALID_CREDENTIALS quando o perfil está inativo")
  void failsWhenProfileInactive() {
    Customer customer = Fixtures.customer(5);
    ReflectionTestUtils.setField(customer, "active", false);
    when(repository.findByEmail(customer.getEmail())).thenReturn(customer);

    assertThatThrownBy(() -> service.execute(new LoginRequest(customer.getEmail(), "1234")))
            .isInstanceOf(AppException.class)
            .extracting(e -> ((AppException) e).getErrorCode())
            .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
    verify(tokenManager, never()).generateToken(any(), any());
  }

  @Test
  @DisplayName("falha com INVALID_CREDENTIALS quando o e-mail não existe")
  void failsWhenEmailNotFound() {
    when(repository.findByEmail("ninguem@web2.com")).thenReturn(null);

    assertThatThrownBy(() -> service.execute(new LoginRequest("ninguem@web2.com", "1234")))
            .isInstanceOf(AppException.class)
            .extracting(exception -> ((AppException) exception).getErrorCode())
            .isEqualTo(ErrorCode.INVALID_CREDENTIALS);

    verify(passwordHasherSalter, never()).matches(any(), any());
  }

  @Test
  @DisplayName("falha com INVALID_CREDENTIALS quando a senha não confere")
  void failsWhenPasswordDoesNotMatch() {
    Customer customer = Fixtures.customer(5);
    when(repository.findByEmail(customer.getEmail())).thenReturn(customer);
    when(passwordHasherSalter.matches(any(), any())).thenReturn(false);

    assertThatThrownBy(() -> service.execute(new LoginRequest(customer.getEmail(), "9999")))
            .isInstanceOf(AppException.class)
            .extracting(exception -> ((AppException) exception).getErrorCode())
            .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
  }
}