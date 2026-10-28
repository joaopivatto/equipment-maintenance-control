package com.web2.equipmentmaintenancecontrol.service.maintenance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentMapper;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentTypeMapper;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.BudgetMapper;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceMapper;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceRequestHistoryMapper;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceRequestMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.CustomerMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.ApproveMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateBudget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.FinishMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RedirectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RejectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentService;
import com.web2.equipmentmaintenancecontrol.service.profile.CustomerService;
import com.web2.equipmentmaintenancecontrol.service.profile.EmployeeService;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@DisplayName("MaintenanceRequestService")
class MaintenanceRequestServiceTest {

  @Mock private MaintenanceRequestRepository repository;
  @Mock private CustomerService customerService;
  @Mock private EquipmentService equipmentService;
  @Mock private EmployeeService employeeService;

  private MaintenanceRequestService service;
  private Customer customer;
  private Employee employee;
  private Employee otherEmployee;

  @BeforeEach
  void setUp() {
    EmployeeMapper employeeMapper = new EmployeeMapper();
    MaintenanceRequestMapper mapper =
        new MaintenanceRequestMapper(
            new EquipmentMapper(new EquipmentTypeMapper()),
            new BudgetMapper(employeeMapper),
            new MaintenanceMapper(employeeMapper),
            new MaintenanceRequestHistoryMapper(employeeMapper),
            new CustomerMapper(),
            employeeMapper);

    service =
        new MaintenanceRequestService(
            repository, mapper, customerService, equipmentService, employeeService);

    customer = Fixtures.customer(1, "Bruno");
    employee = Fixtures.employee(10, "Ana");
    otherEmployee = Fixtures.employee(11, "Bia");
  }

  private void echoOnSave() {
    when(repository.save(any(MaintenanceRequest.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Nested
  @DisplayName("abertura de solicitação")
  class Create {

    @Test
    @DisplayName("abre solicitação ABERTA para o cliente, com equipamento novo e data de agora")
    void createsRequest() {
      LocalDateTime before = LocalDateTime.now(BaseService.APPLICATION_ZONE).minusMinutes(1);
      when(customerService.findById(1)).thenReturn(customer);
      when(equipmentService.newEquipment("Dell Inspiron", 7)).thenReturn(Fixtures.equipment(50));
      echoOnSave();

      MaintenanceRequestDetails details =
          service.create(
              new CreateMaintenanceRequest(
                  1, new EquipmentRequest("Dell Inspiron", 7), "Não liga"));

      ArgumentCaptor<MaintenanceRequest> captor = ArgumentCaptor.forClass(MaintenanceRequest.class);
      verify(repository).save(captor.capture());
      MaintenanceRequest saved = captor.getValue();

      assertThat(saved.getStatus()).isEqualTo(MaintenanceRequestStatus.ABERTA);
      assertThat(saved.getCustomer()).isSameAs(customer);
      assertThat(saved.getDefect()).isEqualTo("Não liga");
      assertThat(saved.getCreatedAt()).isAfter(before);
      assertThat(saved.getCreatedAt()).isEqualTo(saved.getUpdatedAt());
      assertThat(saved.getEmployee()).isNull();

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.ABERTA);
      assertThat(details.defectDescription()).isEqualTo("Não liga");
      assertThat(details.customer().name()).isEqualTo("Bruno");
      assertThat(details.equipment().description()).isEqualTo("Notebook Dell Inspiron");
      assertThat(details.history()).hasSize(1);
    }

    @Test
    @DisplayName("não abre solicitação para cliente inexistente")
    void failsForMissingCustomer() {
      when(customerService.findById(99)).thenThrow(new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

      assertThatThrownBy(
              () ->
                  service.create(
                      new CreateMaintenanceRequest(
                          99, new EquipmentRequest("Dell Inspiron", 7), "Não liga")))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.CUSTOMER_NOT_FOUND);

      verifyNoInteractions(equipmentService);
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("não abre solicitação para categoria de equipamento inexistente")
    void failsForMissingEquipmentType() {
      when(customerService.findById(1)).thenReturn(customer);
      when(equipmentService.newEquipment("Dell Inspiron", 99))
          .thenThrow(new AppException(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND));

      assertThatThrownBy(
              () ->
                  service.create(
                      new CreateMaintenanceRequest(
                          1, new EquipmentRequest("Dell Inspiron", 99), "Não liga")))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("consultas")
  class Queries {

    @Test
    @DisplayName("busca solicitação por id")
    void findsById() {
      when(repository.findById(1)).thenReturn(Optional.of(Fixtures.openRequest(1, customer)));

      assertThat(service.findById(1).id()).isEqualTo(1);
    }

    @Test
    @DisplayName("falha com MAINTENANCE_REQUEST_NOT_FOUND detalhando o id")
    void failsWhenNotFound() {
      when(repository.findById(99)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.findById(99))
          .isInstanceOf(AppException.class)
          .hasMessageContaining("99")
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_REQUEST_NOT_FOUND);
    }

    @Test
    @DisplayName("lista as solicitações do cliente validando que ele existe")
    void listsByCustomer() {
      when(customerService.findById(1)).thenReturn(customer);
      when(repository.findByCustomerIdOrderByCreatedAtAsc(1))
          .thenReturn(
              List.of(Fixtures.openRequest(1, customer), Fixtures.openRequest(2, customer)));

      assertThat(service.findByCustomerId(1))
          .extracting(MaintenanceRequestDetails::id)
          .containsExactly(1, 2);

      verify(customerService).findById(1);
    }

    @Test
    @DisplayName("não lista solicitações de cliente inexistente")
    void failsListingForMissingCustomer() {
      when(customerService.findById(99)).thenThrow(new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

      assertThatThrownBy(() -> service.findByCustomerId(99))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.CUSTOMER_NOT_FOUND);

      verify(repository, never()).findByCustomerIdOrderByCreatedAtAsc(any());
    }

    @Test
    @DisplayName("lista apenas as solicitações em aberto")
    void listsOpenRequests() {
      when(repository.findByStatus(MaintenanceRequestStatus.ABERTA))
          .thenReturn(List.of(Fixtures.openRequest(1, customer)));

      assertThat(service.findOpenRequests())
          .singleElement()
          .extracting(MaintenanceRequestDetails::status)
          .isEqualTo(MaintenanceRequestStatus.ABERTA);

      verify(repository).findByStatus(MaintenanceRequestStatus.ABERTA);
    }

    @Test
    @DisplayName("lista todas as solicitações")
    void listsAll() {
      when(repository.findAll())
          .thenReturn(
              List.of(
                  Fixtures.openRequest(1, customer),
                  Fixtures.finishedRequest(2, customer, employee)));

      assertThat(service.findAll())
          .extracting(MaintenanceRequestDetails::status)
          .containsExactly(MaintenanceRequestStatus.ABERTA, MaintenanceRequestStatus.FINALIZADA);
    }

    @Test
    @DisplayName("devolve lista vazia quando não há solicitações")
    void listsEmpty() {
      when(repository.findAll()).thenReturn(List.of());

      assertThat(service.findAll()).isEmpty();
    }
  }

  @Nested
  @DisplayName("orçamento")
  class GiveBudget {

    @Test
    @DisplayName("registra o orçamento e atribui o funcionário responsável")
    void givesBudget() {
      when(repository.findById(1)).thenReturn(Optional.of(Fixtures.openRequest(1, customer)));
      when(employeeService.findEntityById(10)).thenReturn(employee);
      echoOnSave();

      MaintenanceRequestDetails details =
          service.giveBudget(1, new CreateBudget(new BigDecimal("450.00"), 10));

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.ORCADA);
      assertThat(details.budget().value()).isEqualByComparingTo(new BigDecimal("450.00"));
      assertThat(details.budget().employee().name()).isEqualTo("Ana");
      assertThat(details.assignedEmployee().id()).isEqualTo(10);
      assertThat(details.budget().createdAt()).isNotBlank();
    }

    @Test
    @DisplayName("recusa segundo orçamento antes mesmo de buscar o funcionário")
    void rejectsSecondBudget() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.budgetedRequest(1, customer, employee)));

      assertThatThrownBy(() -> service.giveBudget(1, new CreateBudget(BigDecimal.TEN, 11)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.BUDGET_ALREADY_GIVEN);

      verifyNoInteractions(employeeService);
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("falha quando o funcionário informado não existe")
    void failsForMissingEmployee() {
      when(repository.findById(1)).thenReturn(Optional.of(Fixtures.openRequest(1, customer)));
      when(employeeService.findEntityById(99))
          .thenThrow(new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

      assertThatThrownBy(() -> service.giveBudget(1, new CreateBudget(BigDecimal.TEN, 99)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_NOT_FOUND);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("aprovação e rejeição pelo cliente")
  class ApproveAndReject {

    @Test
    @DisplayName("aprova o orçamento")
    void approves() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.budgetedRequest(1, customer, employee)));
      echoOnSave();

      MaintenanceRequestDetails details = service.approve(1, new ApproveMaintenanceRequest(1));

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.APROVADA);
    }

    @Test
    @DisplayName("impede aprovação por cliente que não é o dono")
    void rejectsApprovalFromAnotherCustomer() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.budgetedRequest(1, customer, employee)));

      assertThatThrownBy(() -> service.approve(1, new ApproveMaintenanceRequest(999)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED);

      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("rejeita o orçamento guardando o motivo")
    void rejects() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.budgetedRequest(1, customer, employee)));
      echoOnSave();

      MaintenanceRequestDetails details =
          service.reject(1, new RejectMaintenanceRequest(1, "Valor acima do esperado"));

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.REJEITADA);
      assertThat(details.rejectionReason()).isEqualTo("Valor acima do esperado");
    }

    @Test
    @DisplayName("impede rejeição por cliente que não é o dono")
    void rejectsRejectionFromAnotherCustomer() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.budgetedRequest(1, customer, employee)));

      assertThatThrownBy(() -> service.reject(1, new RejectMaintenanceRequest(999, "Caro")))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("execução da manutenção")
  class PerformMaintenance {

    @Test
    @DisplayName("registra a manutenção e as orientações ao cliente")
    void performsMaintenance() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.approvedRequest(1, customer, employee)));
      when(employeeService.findEntityById(10)).thenReturn(employee);
      echoOnSave();

      MaintenanceRequestDetails details =
          service.performMaintenance(
              1, new CreateMaintenance("Troca da fonte", "Usar estabilizador", 10));

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.ARRUMADA);
      assertThat(details.maintenance().description()).isEqualTo("Troca da fonte");
      assertThat(details.maintenance().instructions()).isEqualTo("Usar estabilizador");
      assertThat(details.maintenance().employee().id()).isEqualTo(10);
    }

    @Test
    @DisplayName("recusa segunda manutenção antes de buscar o funcionário")
    void rejectsSecondMaintenance() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.fixedRequest(1, customer, employee)));

      assertThatThrownBy(
              () -> service.performMaintenance(1, new CreateMaintenance("Outra", "Outra", 10)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_ALREADY_PERFORMED);

      verifyNoInteractions(employeeService);
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("impede manutenção em solicitação que ainda não foi aprovada")
    void rejectsBeforeApproval() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.budgetedRequest(1, customer, employee)));
      when(employeeService.findEntityById(10)).thenReturn(employee);

      assertThatThrownBy(
              () -> service.performMaintenance(1, new CreateMaintenance("Troca", "Usar", 10)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("redirecionamento")
  class Redirect {

    @Test
    @DisplayName("transfere a solicitação para outro funcionário")
    void redirects() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.approvedRequest(1, customer, employee)));
      when(employeeService.findEntityById(10)).thenReturn(employee);
      when(employeeService.findEntityById(11)).thenReturn(otherEmployee);
      echoOnSave();

      MaintenanceRequestDetails details =
          service.redirect(1, new RedirectMaintenanceRequest(10, 11));

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.REDIRECIONADA);
      assertThat(details.assignedEmployee().id()).isEqualTo(11);
    }

    @Test
    @DisplayName("impede redirecionar para o funcionário que já atende")
    void rejectsSelfRedirect() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.approvedRequest(1, customer, employee)));
      when(employeeService.findEntityById(10)).thenReturn(employee);

      assertThatThrownBy(() -> service.redirect(1, new RedirectMaintenanceRequest(10, 10)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.SELF_REDIRECT_NOT_ALLOWED);

      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("falha quando o funcionário de destino não existe")
    void failsForMissingDestinationEmployee() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.approvedRequest(1, customer, employee)));
      when(employeeService.findEntityById(10)).thenReturn(employee);
      when(employeeService.findEntityById(99))
          .thenThrow(new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

      assertThatThrownBy(() -> service.redirect(1, new RedirectMaintenanceRequest(10, 99)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.EMPLOYEE_NOT_FOUND);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("pagamento e finalização")
  class PayAndFinish {

    @Test
    @DisplayName("registra o pagamento com a data do momento")
    void pays() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.fixedRequest(1, customer, employee)));
      echoOnSave();

      MaintenanceRequestDetails details = service.pay(1);

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.PAGA);
      assertThat(details.paidAt()).isNotBlank();
    }

    @Test
    @DisplayName("impede pagamento antes da manutenção")
    void rejectsPaymentBeforeMaintenance() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.approvedRequest(1, customer, employee)));

      assertThatThrownBy(() -> service.pay(1))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);

      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("finaliza registrando quem finalizou")
    void finishes() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.paidRequest(1, customer, employee)));
      when(employeeService.findEntityById(11)).thenReturn(otherEmployee);
      echoOnSave();

      MaintenanceRequestDetails details = service.finish(1, new FinishMaintenanceRequest(11));

      assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.FINALIZADA);
      assertThat(details.finalizedBy().id()).isEqualTo(11);
      assertThat(details.finalizedAt()).isNotBlank();
    }

    @Test
    @DisplayName("impede finalização antes do pagamento")
    void rejectsFinishBeforePayment() {
      when(repository.findById(1))
          .thenReturn(Optional.of(Fixtures.fixedRequest(1, customer, employee)));
      when(employeeService.findEntityById(10)).thenReturn(employee);

      assertThatThrownBy(() -> service.finish(1, new FinishMaintenanceRequest(10)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);

      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("fluxo completo")
  class FullFlow {

    @Test
    @DisplayName("percorre abertura, orçamento, aprovação, manutenção, pagamento e finalização")
    void walksThroughTheWholeLifecycle() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);
      when(repository.findById(1)).thenReturn(Optional.of(request));
      when(employeeService.findEntityById(10)).thenReturn(employee);
      echoOnSave();

      assertThat(service.giveBudget(1, new CreateBudget(new BigDecimal("450.00"), 10)).status())
          .isEqualTo(MaintenanceRequestStatus.ORCADA);
      assertThat(service.approve(1, new ApproveMaintenanceRequest(1)).status())
          .isEqualTo(MaintenanceRequestStatus.APROVADA);
      assertThat(
              service
                  .performMaintenance(1, new CreateMaintenance("Troca da fonte", "Usar", 10))
                  .status())
          .isEqualTo(MaintenanceRequestStatus.ARRUMADA);
      assertThat(service.pay(1).status()).isEqualTo(MaintenanceRequestStatus.PAGA);

      MaintenanceRequestDetails finished = service.finish(1, new FinishMaintenanceRequest(10));

      assertThat(finished.status()).isEqualTo(MaintenanceRequestStatus.FINALIZADA);
      assertThat(finished.history())
          .hasSize(6)
          .extracting(entry -> entry.status().name())
          .containsExactly("ABERTA", "ORCADA", "APROVADA", "ARRUMADA", "PAGA", "FINALIZADA");
    }
  }
}
