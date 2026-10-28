package com.web2.equipmentmaintenancecontrol.service.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeCreateRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeUpdateRequest;
import com.web2.equipmentmaintenancecontrol.repository.profile.EmployeeRepository;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService")
class EmployeeServiceTest {

  private static final HashedSaltedPassword HASHED = new HashedSaltedPassword("hash", "salt");

  @Mock private EmployeeRepository repository;
  @Mock private PasswordHasherSalter passwordHasherSalter;

  private EmployeeService service;

  @BeforeEach
  void setUp() {
    service = new EmployeeService(repository, new EmployeeMapper(), passwordHasherSalter);
  }

  @Nested
  @DisplayName("consultas")
  class Queries {

    @Test
    @DisplayName("busca funcionário por id")
    void findsById() {
      when(repository.findById(10)).thenReturn(Optional.of(Fixtures.employee(10, "Ana")));

      EmployeeResponse response = service.findById(10);

      assertThat(response.id()).isEqualTo(10);
      assertThat(response.name()).isEqualTo("Ana");
      assertThat(response.birthDate()).isEqualTo(LocalDate.of(1995, 6, 15));
    }

    @Test
    @DisplayName("falha com EMPLOYEE_NOT_FOUND quando o id não existe")
    void failsWhenNotFound() {
      when(repository.findById(99)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.findById(99))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_NOT_FOUND);
    }

    @Test
    @DisplayName("lista os funcionários ativos")
    void listsAll() {
      when(repository.findAll())
          .thenReturn(List.of(Fixtures.employee(1, "Ana"), Fixtures.employee(2, "Bia")));

      assertThat(service.findAll())
          .extracting(EmployeeResponse::name)
          .containsExactly("Ana", "Bia");
    }

    @Test
    @DisplayName("devolve lista vazia quando não há funcionários")
    void listsEmpty() {
      when(repository.findAll()).thenReturn(List.of());

      assertThat(service.findAll()).isEmpty();
    }
  }

  @Nested
  @DisplayName("criação")
  class Create {

    @Test
    @DisplayName("cria funcionário com a senha já transformada em hash e salt")
    void createsEmployee() {
      EmployeeCreateRequest request =
          new EmployeeCreateRequest("ana@web2.com", "Ana", LocalDate.of(1990, 2, 3), "1234");
      when(passwordHasherSalter.hashAndSalt("1234")).thenReturn(HASHED);
      when(repository.save(any(Employee.class)))
          .thenAnswer(invocation -> Fixtures.withId(invocation.getArgument(0), 20));

      EmployeeResponse response = service.create(request);

      ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
      verify(repository).save(captor.capture());
      Employee saved = captor.getValue();

      assertThat(saved.getName()).isEqualTo("Ana");
      assertThat(saved.getEmail()).isEqualTo("ana@web2.com");
      assertThat(saved.getBirthDate()).isEqualTo(LocalDate.of(1990, 2, 3));
      assertThat(saved.getType()).isEqualTo(ProfileType.EMPLOYEE);
      assertThat(saved.getPasswordHash()).isEqualTo("hash");
      assertThat(saved.getPasswordSalt()).isEqualTo("salt");
      assertThat(saved.getActive()).isTrue();
      assertThat(response.id()).isEqualTo(20);
    }
  }

  @Nested
  @DisplayName("atualização")
  class Update {

    @Test
    @DisplayName("atualiza nome, e-mail e data de nascimento preservando a senha")
    void updatesEmployee() {
      Employee existing = Fixtures.employee(10, "Ana");
      when(repository.findById(10)).thenReturn(Optional.of(existing));
      when(repository.save(existing)).thenReturn(existing);

      EmployeeResponse response =
          service.update(
              10,
              new EmployeeUpdateRequest(
                  "ana.nova@web2.com", "Ana Maria", LocalDate.of(1991, 1, 2)));

      assertThat(existing.getName()).isEqualTo("Ana Maria");
      assertThat(existing.getEmail()).isEqualTo("ana.nova@web2.com");
      assertThat(existing.getBirthDate()).isEqualTo(LocalDate.of(1991, 1, 2));
      assertThat(existing.getPasswordHash()).isEqualTo("hash");
      assertThat(response.name()).isEqualTo("Ana Maria");
    }

    @Test
    @DisplayName("não atualiza funcionário inexistente")
    void failsForMissingEmployee() {
      when(repository.findById(99)).thenReturn(Optional.empty());

      assertThatThrownBy(
              () ->
                  service.update(
                      99, new EmployeeUpdateRequest("x@web2.com", "X", LocalDate.of(1990, 1, 1))))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_NOT_FOUND);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("inativação")
  class Delete {

    @Test
    @DisplayName("inativa outro funcionário quando ainda sobra alguém ativo")
    void deactivatesEmployee() {
      Employee target = Fixtures.employee(10);
      when(repository.findById(10)).thenReturn(Optional.of(target));
      when(repository.countByActiveTrue()).thenReturn(3L);

      service.delete(10, 11);

      verify(repository).delete(target);
    }

    @Test
    @DisplayName("impede que o funcionário inative a própria conta")
    void rejectsSelfDeactivation() {
      when(repository.findById(10)).thenReturn(Optional.of(Fixtures.employee(10)));

      assertThatThrownBy(() -> service.delete(10, 10))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_CANNOT_DEACTIVATE_SELF);

      verify(repository, never()).delete(any(Employee.class));
    }

    @Test
    @DisplayName("falha ao inativar funcionário já inativo")
    void rejectsAlreadyInactive() {
      when(repository.findById(10))
          .thenReturn(Optional.of(Fixtures.deactivate(Fixtures.employee(10))));

      assertThatThrownBy(() -> service.delete(10, 11))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_ALREADY_INACTIVE);

      verify(repository, never()).delete(any(Employee.class));
    }

    @Test
    @DisplayName("impede inativar o último funcionário ativo")
    void rejectsLastActiveEmployee() {
      when(repository.findById(10)).thenReturn(Optional.of(Fixtures.employee(10)));
      when(repository.countByActiveTrue()).thenReturn(1L);

      assertThatThrownBy(() -> service.delete(10, 11))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_LAST_ACTIVE);

      verify(repository, never()).delete(any(Employee.class));
    }

    @Test
    @DisplayName("falha ao inativar funcionário inexistente")
    void rejectsMissingEmployee() {
      when(repository.findById(99)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.delete(99, 11))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_NOT_FOUND);
    }
  }
}
