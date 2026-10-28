package com.web2.equipmentmaintenancecontrol.model.maintenance;

import static com.web2.equipmentmaintenancecontrol.support.Fixtures.AT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MaintenanceRequest (máquina de estados)")
class MaintenanceRequestTest {

  private Customer customer;
  private Employee employee;
  private Employee otherEmployee;

  @BeforeEach
  void setUp() {
    customer = Fixtures.customer(1);
    employee = Fixtures.employee(10);
    otherEmployee = Fixtures.employee(11);
  }

  @Nested
  @DisplayName("criação")
  class Creation {

    @Test
    @DisplayName("nasce ABERTA, com histórico inicial e updatedAt igual a createdAt")
    void startsOpen() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.ABERTA);
      assertThat(request.getCreatedAt()).isEqualTo(AT);
      assertThat(request.getUpdatedAt()).isEqualTo(AT);
      assertThat(request.getDefect()).isEqualTo("Não liga");
      assertThat(request.getCustomer()).isSameAs(customer);
      assertThat(request.getEmployee()).isNull();
      assertThat(request.getBudget()).isNull();
      assertThat(request.getMaintenance()).isNull();
      assertThat(request.getRedirects()).isEmpty();
      assertThat(request.getHistory()).hasSize(1);
      assertThat(request.getHistory().getFirst().getStatus())
          .isEqualTo(MaintenanceRequestStatus.ABERTA);
      assertThat(request.getHistory().getFirst().getMaintenanceRequest()).isSameAs(request);
    }

    @Test
    @DisplayName("exige cliente, equipamento, defeito e data de criação")
    void requiresMandatoryFields() {
      assertThatThrownBy(
              () ->
                  MaintenanceRequest.builder()
                      .equipment(Fixtures.equipment(1))
                      .defect("Não liga")
                      .createdAt(AT)
                      .build())
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("customer");

      assertThatThrownBy(
              () ->
                  MaintenanceRequest.builder()
                      .customer(customer)
                      .defect("Não liga")
                      .createdAt(AT)
                      .build())
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("equipment");

      assertThatThrownBy(
              () ->
                  MaintenanceRequest.builder()
                      .customer(customer)
                      .equipment(Fixtures.equipment(1))
                      .createdAt(AT)
                      .build())
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("defect");

      assertThatThrownBy(
              () ->
                  MaintenanceRequest.builder()
                      .customer(customer)
                      .equipment(Fixtures.equipment(1))
                      .defect("Não liga")
                      .build())
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("createdAt");
    }
  }

  @Nested
  @DisplayName("orçamento")
  class GiveBudget {

    @Test
    @DisplayName("passa para ORCADA e atribui o funcionário responsável")
    void movesToBudgeted() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);
      LocalDateTime budgetedAt = AT.plusHours(1);

      request.giveBudget(Fixtures.budget(5, employee), budgetedAt);

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.ORCADA);
      assertThat(request.getBudget()).isNotNull();
      assertThat(request.getBudget().getValue()).isEqualByComparingTo(new BigDecimal("450.00"));
      assertThat(request.getEmployee()).isSameAs(employee);
      assertThat(request.getUpdatedAt()).isEqualTo(budgetedAt);
      assertThat(request.getHistory()).hasSize(2);
      assertThat(request.getHistory().getLast().getEmployee()).isSameAs(employee);
    }

    @Test
    @DisplayName("recusa segundo orçamento")
    void rejectsSecondBudget() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.giveBudget(Fixtures.budget(6, employee), AT.plusHours(2)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.BUDGET_ALREADY_GIVEN);
    }

    @Test
    @DisplayName("exige orçamento não nulo")
    void requiresBudget() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);

      assertThatThrownBy(() -> request.giveBudget(null, AT))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("Orçamento");
    }

    @Test
    @DisplayName("não permite orçar solicitação já aprovada")
    void rejectsBudgetOnInvalidStatus() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.giveBudget(Fixtures.budget(6, employee), AT.plusHours(3)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.BUDGET_ALREADY_GIVEN);
    }
  }

  @Nested
  @DisplayName("aprovação e rejeição")
  class ApproveAndReject {

    @Test
    @DisplayName("aprova orçamento do próprio cliente")
    void approvesBudget() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);

      request.approve(AT.plusHours(2), customer.getId());

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.APROVADA);
      assertThat(request.getUpdatedAt()).isEqualTo(AT.plusHours(2));
      assertThat(request.getHistory().getLast().getEmployee()).isNull();
    }

    @Test
    @DisplayName("impede aprovação por outro cliente")
    void rejectsApprovalFromAnotherCustomer() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.approve(AT.plusHours(2), 999))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED);

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.ORCADA);
    }

    @Test
    @DisplayName("impede aprovação de solicitação ainda ABERTA")
    void rejectsApprovalBeforeBudget() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);

      assertThatThrownBy(() -> request.approve(AT.plusHours(1), customer.getId()))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("rejeita orçamento guardando o motivo")
    void rejectsBudgetWithReason() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);

      request.reject("Valor acima do esperado", AT.plusHours(2), customer.getId());

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.REJEITADA);
      assertThat(request.getRejectionReason()).isEqualTo("Valor acima do esperado");
      assertThat(request.getHistory().getLast().getReason()).isEqualTo("Valor acima do esperado");
    }

    @Test
    @DisplayName("exige motivo na rejeição")
    void requiresRejectionReason() {
      MaintenanceRequest blank = Fixtures.budgetedRequest(1, customer, employee);
      MaintenanceRequest nullReason = Fixtures.budgetedRequest(2, customer, employee);

      assertThatThrownBy(() -> blank.reject("   ", AT.plusHours(2), customer.getId()))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.VALIDATION_ERROR);

      assertThatThrownBy(() -> nullReason.reject(null, AT.plusHours(2), customer.getId()))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    @DisplayName("impede rejeição por outro cliente antes de validar o motivo")
    void rejectsRejectionFromAnotherCustomer() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.reject(null, AT.plusHours(2), 999))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED);
    }

    @Test
    @DisplayName("permite resgatar serviço rejeitado aprovando depois")
    void allowsApprovalAfterRejection() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);
      request.reject("Muito caro", AT.plusHours(2), customer.getId());

      request.approve(AT.plusHours(3), customer.getId());

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.APROVADA);
      assertThat(request.getRejectionReason()).isEqualTo("Muito caro");
    }
  }

  @Nested
  @DisplayName("manutenção")
  class PerformMaintenance {

    @Test
    @DisplayName("passa para ARRUMADA e registra o funcionário executor")
    void movesToFixed() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      request.performMaintenance(Fixtures.maintenance(9, otherEmployee), AT.plusHours(3));

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.ARRUMADA);
      assertThat(request.getMaintenance()).isNotNull();
      assertThat(request.getEmployee()).isSameAs(otherEmployee);
    }

    @Test
    @DisplayName("recusa segunda manutenção")
    void rejectsSecondMaintenance() {
      MaintenanceRequest request = Fixtures.fixedRequest(1, customer, employee);

      assertThatThrownBy(
              () -> request.performMaintenance(Fixtures.maintenance(9, employee), AT.plusHours(4)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.MAINTENANCE_ALREADY_PERFORMED);
    }

    @Test
    @DisplayName("impede manutenção antes da aprovação")
    void rejectsMaintenanceBeforeApproval() {
      MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);

      assertThatThrownBy(
              () -> request.performMaintenance(Fixtures.maintenance(9, employee), AT.plusHours(3)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("exige manutenção não nula")
    void requiresMaintenance() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.performMaintenance(null, AT.plusHours(3)))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("Manutenção");
    }
  }

  @Nested
  @DisplayName("redirecionamento")
  class RedirectTo {

    @Test
    @DisplayName("transfere a solicitação e guarda o registro do redirecionamento")
    void redirectsToAnotherEmployee() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);
      LocalDateTime redirectedAt = AT.plusHours(3);

      request.redirectTo(new Redirect(employee, otherEmployee), redirectedAt);

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.REDIRECIONADA);
      assertThat(request.getEmployee()).isSameAs(otherEmployee);
      assertThat(request.getRedirects()).hasSize(1);

      Redirect redirect = request.getRedirects().getFirst();
      assertThat(redirect.getSourceEmployee()).isSameAs(employee);
      assertThat(redirect.getDestinationEmployee()).isSameAs(otherEmployee);
      assertThat(redirect.getCreatedAt()).isEqualTo(redirectedAt);
      assertThat(redirect.getMaintenanceRequest()).isSameAs(request);
    }

    @Test
    @DisplayName("permite redirecionamentos em sequência")
    void allowsChainedRedirects() {
      Employee third = Fixtures.employee(12);
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      request.redirectTo(new Redirect(employee, otherEmployee), AT.plusHours(3));
      request.redirectTo(new Redirect(otherEmployee, third), AT.plusHours(4));

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.REDIRECIONADA);
      assertThat(request.getEmployee()).isSameAs(third);
      assertThat(request.getRedirects()).hasSize(2);
    }

    @Test
    @DisplayName("impede redirecionar para o funcionário que já atende a solicitação")
    void rejectsSelfRedirect() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      assertThatThrownBy(
              () -> request.redirectTo(new Redirect(employee, employee), AT.plusHours(3)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.SELF_REDIRECT_NOT_ALLOWED);
    }

    @Test
    @DisplayName("permite manutenção após redirecionamento")
    void allowsMaintenanceAfterRedirect() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);
      request.redirectTo(new Redirect(employee, otherEmployee), AT.plusHours(3));

      request.performMaintenance(Fixtures.maintenance(9, otherEmployee), AT.plusHours(4));

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.ARRUMADA);
    }

    @Test
    @DisplayName("impede redirecionar solicitação ainda ABERTA")
    void rejectsRedirectOnOpenRequest() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);

      assertThatThrownBy(
              () -> request.redirectTo(new Redirect(employee, otherEmployee), AT.plusHours(1)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("exige redirecionamento e data não nulos")
    void requiresRedirectAndDateTime() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.redirectTo(null, AT))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("Redirecionamento");

      assertThatThrownBy(() -> request.redirectTo(new Redirect(employee, otherEmployee), null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("pagamento e finalização")
  class PayAndFinish {

    @Test
    @DisplayName("registra a data do pagamento")
    void paysRequest() {
      MaintenanceRequest request = Fixtures.fixedRequest(1, customer, employee);
      LocalDateTime paidAt = AT.plusHours(4);

      request.pay(paidAt);

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.PAGA);
      assertThat(request.getPaymentDate()).isEqualTo(paidAt);
    }

    @Test
    @DisplayName("impede pagamento antes da manutenção")
    void rejectsPaymentBeforeMaintenance() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.pay(AT.plusHours(4)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("finaliza registrando quem finalizou e quando")
    void finishesRequest() {
      MaintenanceRequest request = Fixtures.paidRequest(1, customer, employee);
      LocalDateTime finishedAt = AT.plusHours(5);

      request.finish(otherEmployee, finishedAt);

      assertThat(request.getStatus()).isEqualTo(MaintenanceRequestStatus.FINALIZADA);
      assertThat(request.getFinalizedBy()).isSameAs(otherEmployee);
      assertThat(request.getFinalizedAt()).isEqualTo(finishedAt);
    }

    @Test
    @DisplayName("impede finalização antes do pagamento")
    void rejectsFinishBeforePayment() {
      MaintenanceRequest request = Fixtures.fixedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.finish(employee, AT.plusHours(5)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("exige funcionário que finaliza")
    void requiresFinishingEmployee() {
      MaintenanceRequest request = Fixtures.paidRequest(1, customer, employee);

      assertThatThrownBy(() -> request.finish(null, AT.plusHours(5)))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("Funcionário");
    }

    @Test
    @DisplayName("solicitação FINALIZADA é um estado terminal")
    void finishedIsTerminal() {
      MaintenanceRequest request = Fixtures.finishedRequest(1, customer, employee);

      assertThatThrownBy(() -> request.pay(AT.plusHours(6)))
          .isInstanceOf(AppException.class)
          .extracting(exception -> ((AppException) exception).getErrorCode())
          .isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }
  }

  @Nested
  @DisplayName("histórico")
  class History {

    @Test
    @DisplayName("acumula uma entrada por transição, em ordem, com updatedAt sincronizado")
    void recordsEveryTransition() {
      MaintenanceRequest request = Fixtures.finishedRequest(1, customer, employee);

      assertThat(request.getHistory())
          .extracting(MaintenanceRequestHistory::getStatus)
          .containsExactly(
              MaintenanceRequestStatus.ABERTA,
              MaintenanceRequestStatus.ORCADA,
              MaintenanceRequestStatus.APROVADA,
              MaintenanceRequestStatus.ARRUMADA,
              MaintenanceRequestStatus.PAGA,
              MaintenanceRequestStatus.FINALIZADA);
      assertThat(request.getUpdatedAt()).isEqualTo(request.getHistory().getLast().getUpdatedAt());
      assertThat(request.getHistory())
          .allSatisfy(entry -> assertThat(entry.getMaintenanceRequest()).isSameAs(request));
    }

    @Test
    @DisplayName("não registra entrada quando a transição é inválida")
    void doesNotRecordInvalidTransition() {
      MaintenanceRequest request = Fixtures.openRequest(1, customer);

      assertThatThrownBy(() -> request.pay(AT.plusHours(1))).isInstanceOf(AppException.class);

      assertThat(request.getHistory()).hasSize(1);
      assertThat(request.getPaymentDate()).isNull();
      assertThat(request.getUpdatedAt()).isEqualTo(AT);
    }
  }
}
