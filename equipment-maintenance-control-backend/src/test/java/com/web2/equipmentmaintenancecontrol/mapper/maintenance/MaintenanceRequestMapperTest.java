package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import static com.web2.equipmentmaintenancecontrol.support.Fixtures.AT;
import static org.assertj.core.api.Assertions.assertThat;

import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentMapper;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentTypeMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.CustomerMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Redirect;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.HistoryEntryDetails;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MaintenanceRequestMapper")
class MaintenanceRequestMapperTest {

  private MaintenanceRequestMapper mapper;
  private Customer customer;
  private Employee employee;
  private Employee otherEmployee;

  @BeforeEach
  void setUp() {
    EmployeeMapper employeeMapper = new EmployeeMapper();
    mapper =
        new MaintenanceRequestMapper(
            new EquipmentMapper(new EquipmentTypeMapper()),
            new BudgetMapper(employeeMapper),
            new MaintenanceMapper(employeeMapper),
            new MaintenanceRequestHistoryMapper(employeeMapper),
            new CustomerMapper(),
            employeeMapper);

    customer = Fixtures.customer(1, "Bruno");
    employee = Fixtures.employee(10, "Ana");
    otherEmployee = Fixtures.employee(11, "Bia");
  }

  @Test
  @DisplayName("converte solicitação nula em null e lista nula em lista vazia")
  void handlesNulls() {
    assertThat(mapper.toDetails((MaintenanceRequest) null)).isNull();
    assertThat(mapper.toDetails((List<MaintenanceRequest>) null)).isEmpty();
  }

  @Test
  @DisplayName("formata datas no padrão dd/MM/yyyy HH:mm e omite o que não aconteceu")
  void mapsOpenRequest() {
    MaintenanceRequestDetails details = mapper.toDetails(Fixtures.openRequest(1, customer));

    assertThat(details.id()).isEqualTo(1);
    assertThat(details.createdAt()).isEqualTo("10/03/2026 09:30");
    assertThat(details.updatedAt()).isEqualTo("10/03/2026 09:30");
    assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.ABERTA);
    assertThat(details.budget()).isNull();
    assertThat(details.maintenance()).isNull();
    assertThat(details.assignedEmployee()).isNull();
    assertThat(details.paidAt()).isNull();
    assertThat(details.finalizedAt()).isNull();
    assertThat(details.finalizedBy()).isNull();
    assertThat(details.rejectionReason()).isNull();
    assertThat(details.customer().name()).isEqualTo("Bruno");
    assertThat(details.equipment().type().description()).isEqualTo("Notebook");
  }

  @Test
  @DisplayName("converte solicitação finalizada com orçamento, manutenção e pagamento")
  void mapsFinishedRequest() {
    MaintenanceRequestDetails details =
        mapper.toDetails(Fixtures.finishedRequest(1, customer, employee));

    assertThat(details.status()).isEqualTo(MaintenanceRequestStatus.FINALIZADA);
    assertThat(details.budget().value()).isEqualByComparingTo(new BigDecimal("450.00"));
    assertThat(details.budget().employee().name()).isEqualTo("Ana");
    assertThat(details.maintenance().description()).isEqualTo("Troca da fonte");
    assertThat(details.maintenance().instructions()).isEqualTo("Evitar uso sem estabilizador");
    assertThat(details.paidAt()).isEqualTo("10/03/2026 13:30");
    assertThat(details.finalizedAt()).isEqualTo("10/03/2026 14:30");
    assertThat(details.finalizedBy().name()).isEqualTo("Ana");
    assertThat(details.history()).hasSize(6);
  }

  @Test
  @DisplayName("expõe o motivo da rejeição")
  void mapsRejectionReason() {
    MaintenanceRequest request = Fixtures.budgetedRequest(1, customer, employee);
    request.reject("Valor acima do esperado", AT.plusHours(2), customer.getId());

    MaintenanceRequestDetails details = mapper.toDetails(request);

    assertThat(details.rejectionReason()).isEqualTo("Valor acima do esperado");
    assertThat(details.history().getLast().reason()).isEqualTo("Valor acima do esperado");
  }

  @Test
  @DisplayName("converte lista de solicitações")
  void mapsList() {
    assertThat(
            mapper.toDetails(
                List.of(
                    Fixtures.openRequest(1, customer),
                    Fixtures.budgetedRequest(2, customer, employee))))
        .extracting(MaintenanceRequestDetails::status)
        .containsExactly(MaintenanceRequestStatus.ABERTA, MaintenanceRequestStatus.ORCADA);
  }

  @Nested
  @DisplayName("histórico")
  class History {

    @Test
    @DisplayName("gera uma entrada por transição, com data formatada e responsável")
    void mapsHistoryEntries() {
      MaintenanceRequestDetails details =
          mapper.toDetails(Fixtures.finishedRequest(1, customer, employee));

      assertThat(details.history())
          .extracting(HistoryEntryDetails::status)
          .containsExactly(
              MaintenanceRequestStatus.ABERTA,
              MaintenanceRequestStatus.ORCADA,
              MaintenanceRequestStatus.APROVADA,
              MaintenanceRequestStatus.ARRUMADA,
              MaintenanceRequestStatus.PAGA,
              MaintenanceRequestStatus.FINALIZADA);

      HistoryEntryDetails budgeted = details.history().get(1);
      assertThat(budgeted.dateTime()).isEqualTo("10/03/2026 10:30");
      assertThat(budgeted.responsible().name()).isEqualTo("Ana");
      assertThat(budgeted.fromEmployee()).isNull();
      assertThat(budgeted.toEmployee()).isNull();
    }

    @Test
    @DisplayName("preenche origem e destino nas entradas de redirecionamento")
    void mapsRedirectEntries() {
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);
      request.redirectTo(new Redirect(employee, otherEmployee), AT.plusHours(3));

      MaintenanceRequestDetails details = mapper.toDetails(request);
      HistoryEntryDetails redirected = details.history().getLast();

      assertThat(redirected.status()).isEqualTo(MaintenanceRequestStatus.REDIRECIONADA);
      assertThat(redirected.fromEmployee().name()).isEqualTo("Ana");
      assertThat(redirected.toEmployee().name()).isEqualTo("Bia");
    }

    @Test
    @DisplayName("casa cada redirecionamento com a sua entrada, em ordem cronológica")
    void pairsChainedRedirectsWithTheirEntries() {
      Employee third = Fixtures.employee(12, "Caio");
      MaintenanceRequest request = Fixtures.approvedRequest(1, customer, employee);
      request.redirectTo(new Redirect(employee, otherEmployee), AT.plusHours(3));
      request.redirectTo(new Redirect(otherEmployee, third), AT.plusHours(4));

      List<HistoryEntryDetails> history = mapper.toDetails(request).history();
      List<HistoryEntryDetails> redirects =
          history.stream()
              .filter(entry -> entry.status() == MaintenanceRequestStatus.REDIRECIONADA)
              .toList();

      assertThat(redirects).hasSize(2);
      assertThat(redirects.getFirst().fromEmployee().name()).isEqualTo("Ana");
      assertThat(redirects.getFirst().toEmployee().name()).isEqualTo("Bia");
      assertThat(redirects.getLast().fromEmployee().name()).isEqualTo("Bia");
      assertThat(redirects.getLast().toEmployee().name()).isEqualTo("Caio");
    }

    @Test
    @DisplayName("devolve histórico vazio para solicitação nula")
    void handlesNullRequest() {
      assertThat(new MaintenanceRequestHistoryMapper(new EmployeeMapper()).toDetails(null))
          .isEmpty();
    }
  }

  @Nested
  @DisplayName("mappers auxiliares")
  class NestedMappers {

    @Test
    @DisplayName("BudgetMapper converte orçamento e aceita nulo")
    void budgetMapper() {
      BudgetMapper budgetMapper = new BudgetMapper(new EmployeeMapper());

      assertThat(budgetMapper.toDetails(null)).isNull();
      assertThat(budgetMapper.toDetails(Fixtures.budget(5, employee)))
          .satisfies(
              details -> {
                assertThat(details.id()).isEqualTo(5);
                assertThat(details.value()).isEqualByComparingTo(new BigDecimal("450.00"));
                assertThat(details.createdAt()).isEqualTo("10/03/2026 09:30");
                assertThat(details.employee().name()).isEqualTo("Ana");
              });
    }

    @Test
    @DisplayName("MaintenanceMapper converte manutenção e aceita nulo")
    void maintenanceMapper() {
      MaintenanceMapper maintenanceMapper = new MaintenanceMapper(new EmployeeMapper());

      assertThat(maintenanceMapper.toDetails(null)).isNull();
      assertThat(maintenanceMapper.toDetails(Fixtures.maintenance(7, employee)))
          .satisfies(
              details -> {
                assertThat(details.id()).isEqualTo(7);
                assertThat(details.description()).isEqualTo("Troca da fonte");
                assertThat(details.instructions()).isEqualTo("Evitar uso sem estabilizador");
                assertThat(details.employee().name()).isEqualTo("Ana");
              });
    }
  }
}
