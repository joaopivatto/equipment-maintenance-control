package com.web2.equipmentmaintenancecontrol.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.domain.email.Email;
import com.web2.equipmentmaintenancecontrol.domain.email.EmailProvider;
import com.web2.equipmentmaintenancecontrol.domain.email.PasswordEmail;
import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordGenerator;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.AddressSignupRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.SignupRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.repository.profile.CustomerRepository;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignupService")
class SignupServiceTest {

  private static final HashedSaltedPassword HASHED = new HashedSaltedPassword("hash", "salt");

  @Mock private CustomerRepository repository;
  @Mock private PasswordGenerator passwordGenerator;
  @Mock private PasswordHasherSalter passwordHasherSalter;
  @Mock private EmailProvider emailProvider;
  @Mock private PasswordEmail passwordEmail;

  private SignupService service;

  @BeforeEach
  void setUp() {
    service =
        new SignupService(
            repository, passwordGenerator, passwordHasherSalter, emailProvider, passwordEmail);
  }

  private SignupRequest request() {
    return new SignupRequest(
        "Bruno Silva",
        "bruno@web2.com",
        "12345678901",
        "41999990000",
        new AddressSignupRequest(
            "Rua das Flores", 100, "Apto 10", "Centro", "Curitiba", "PR", "80010000"));
  }

  @Test
  @DisplayName("cadastra cliente com senha gerada, salva e envia e-mail")
  void registersCustomer() {
    Email email = new Email("no-reply@web2.com", "bruno@web2.com", "Senha", "<p>1234</p>");
    when(repository.findByCpf("12345678901")).thenReturn(null);
    when(repository.findByEmail("bruno@web2.com")).thenReturn(null);
    when(passwordGenerator.generate(4)).thenReturn("1234");
    when(passwordHasherSalter.hashAndSalt("1234")).thenReturn(HASHED);
    when(passwordEmail.build("bruno@web2.com", "1234")).thenReturn(email);

    service.execute(request());

    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(repository).save(captor.capture());
    Customer saved = captor.getValue();

    assertThat(saved.getName()).isEqualTo("Bruno Silva");
    assertThat(saved.getEmail()).isEqualTo("bruno@web2.com");
    assertThat(saved.getCpf()).isEqualTo("12345678901");
    assertThat(saved.getType()).isEqualTo(ProfileType.CUSTOMER);
    assertThat(saved.getPasswordHash()).isEqualTo("hash");
    assertThat(saved.getPasswordSalt()).isEqualTo("salt");
    assertThat(saved.getActive()).isTrue();
    assertThat(saved.getPhone().getNumber()).isEqualTo("41999990000");

    verify(emailProvider).dispatch(email);
  }

  @Test
  @DisplayName("copia todos os campos do endereço informado")
  void mapsAddressFields() {
    when(passwordGenerator.generate(anyInt())).thenReturn("1234");
    when(passwordHasherSalter.hashAndSalt(anyString())).thenReturn(HASHED);

    service.execute(request());

    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(repository).save(captor.capture());

    assertThat(captor.getValue().getAddress())
        .satisfies(
            address -> {
              assertThat(address.getZipCode()).isEqualTo("80010000");
              assertThat(address.getStreet()).isEqualTo("Rua das Flores");
              assertThat(address.getNumber()).isEqualTo(100);
              assertThat(address.getComplement()).isEqualTo("Apto 10");
              assertThat(address.getNeighborhood()).isEqualTo("Centro");
              assertThat(address.getCity()).isEqualTo("Curitiba");
              assertThat(address.getState()).isEqualTo("PR");
            });
  }

  @Test
  @DisplayName("gera senha de 4 dígitos e nunca a persiste em claro")
  void generatesFourDigitPassword() {
    when(passwordGenerator.generate(4)).thenReturn("8742");
    when(passwordHasherSalter.hashAndSalt("8742")).thenReturn(HASHED);

    service.execute(request());

    verify(passwordGenerator).generate(4);
    verify(passwordHasherSalter).hashAndSalt("8742");
    verify(passwordEmail).build("bruno@web2.com", "8742");
  }

  @Test
  @DisplayName("falha quando o CPF já está cadastrado")
  void failsWhenCpfAlreadyExists() {
    when(repository.findByCpf("12345678901")).thenReturn(Fixtures.customer(1));

    assertThatThrownBy(() -> service.execute(request()))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.CUSTOMER_ALREADY_EXISTS);

    verify(repository, never()).save(any());
    verifyNoInteractions(passwordGenerator, emailProvider);
  }

  @Test
  @DisplayName("falha quando o e-mail já está cadastrado")
  void failsWhenEmailAlreadyExists() {
    when(repository.findByCpf("12345678901")).thenReturn(null);
    when(repository.findByEmail("bruno@web2.com")).thenReturn(Fixtures.customer(1));

    assertThatThrownBy(() -> service.execute(request()))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.CUSTOMER_EMAIL_ALREADY_EXISTS);

    verify(repository, never()).save(any());
    verifyNoInteractions(passwordGenerator, emailProvider);
  }

  @Test
  @DisplayName("propaga falha do provedor de e-mail para que a transação seja revertida")
  void propagatesEmailProviderFailure() {
    when(passwordGenerator.generate(anyInt())).thenReturn("1234");
    when(passwordHasherSalter.hashAndSalt(anyString())).thenReturn(HASHED);
    org.mockito.Mockito.doThrow(new AppException(ErrorCode.EMAIL_PROVIDER_INTEGRATION_ERROR))
        .when(emailProvider)
        .dispatch(any());

    assertThatThrownBy(() -> service.execute(request()))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.EMAIL_PROVIDER_INTEGRATION_ERROR);
  }
}
