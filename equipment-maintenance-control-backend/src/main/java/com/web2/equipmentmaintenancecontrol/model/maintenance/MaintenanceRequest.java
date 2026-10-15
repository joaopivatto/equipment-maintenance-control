package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class MaintenanceRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String defect;

  @Nullable private LocalDateTime paymentDate;

  @Nullable private String rejectionReason;

  @Nullable private LocalDateTime finalizedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MaintenanceRequestStatus status;

  @ManyToOne
  @JoinColumn(name = "equipment_id", nullable = false)
  private Equipment equipment;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  @Nullable
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "finalized_by_id")
  @Nullable
  private Employee finalizedBy;

  @OneToOne
  @JoinColumn(name = "budget_id")
  @Nullable
  private Budget budget;

  @OneToOne
  @JoinColumn(name = "maintenance_id")
  @Nullable
  private Maintenance maintenance;

  @OneToMany(mappedBy = "maintenanceRequest", cascade = CascadeType.ALL)
  @OrderBy("updatedAt DESC")
  private List<MaintenanceRequestHistory> history;

  @OneToMany(mappedBy = "maintenanceRequest", cascade = CascadeType.ALL)
  @OrderBy("createdAt ASC")
  private List<Redirect> redirects;

  public MaintenanceRequest() {}

  private MaintenanceRequest(Builder builder) {
    this.customer = Objects.requireNonNull(builder.customer, "customer é obrigatório");
    this.equipment = Objects.requireNonNull(builder.equipment, "equipment é obrigatório");
    this.defect = Objects.requireNonNull(builder.defect, "defect é obrigatório");
    LocalDateTime createdAt = Objects.requireNonNull(builder.createdAt, "createdAt é obrigatório");
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.employee = builder.employee;
    this.status = MaintenanceRequestStatus.ABERTA;
    createHistory(createdAt, builder.employee);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private Customer customer;
    private Equipment equipment;
    private String defect;
    private LocalDateTime createdAt;
    private Employee employee;

    private Builder() {}

    public Builder customer(Customer customer) {
      this.customer = customer;
      return this;
    }

    public Builder equipment(Equipment equipment) {
      this.equipment = equipment;
      return this;
    }

    public Builder defect(String defect) {
      this.defect = defect;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder employee(@Nullable Employee employee) {
      this.employee = employee;
      return this;
    }

    public MaintenanceRequest build() {
      return new MaintenanceRequest(this);
    }
  }

  public void giveBudget(Budget budget, LocalDateTime dateTime) {
    Objects.requireNonNull(budget, "Orçamento é obrigatório");
    requireNoBudget();
    Employee responsible =
        Objects.requireNonNull(budget.getEmployee(), "Funcionário do orçamento é obrigatório");

    transitionTo(MaintenanceRequestStatus.ORCADA, responsible, dateTime);
    this.budget = budget;
    this.employee = responsible;
  }

  public void approve(LocalDateTime dateTime, Integer customerId) {
    requireOwner(customerId);

    transitionTo(MaintenanceRequestStatus.APROVADA, null, dateTime);
  }

  public void reject(String reason, LocalDateTime dateTime, Integer customerId) {
    requireOwner(customerId);
    if (reason == null || reason.isBlank()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR, "O motivo da rejeição é obrigatório.");
    }

    transitionTo(MaintenanceRequestStatus.REJEITADA, null, dateTime, reason);
    this.rejectionReason = reason;
  }

  public void performMaintenance(Maintenance maintenance, LocalDateTime dateTime) {
    Objects.requireNonNull(maintenance, "Manutenção é obrigatória");
    requireNoMaintenance();
    Employee responsible =
        Objects.requireNonNull(
            maintenance.getEmployee(), "Funcionário da manutenção é obrigatório");

    transitionTo(MaintenanceRequestStatus.ARRUMADA, responsible, dateTime);
    this.maintenance = maintenance;
    this.employee = responsible;
  }

  public void requireNoBudget() {
    if (budget != null) {
      throw new AppException(
          ErrorCode.BUDGET_ALREADY_GIVEN,
          "A solicitação %d já possui o orçamento %d".formatted(id, budget.getId()));
    }
  }

  public void requireNoMaintenance() {
    if (maintenance != null) {
      throw new AppException(
          ErrorCode.MAINTENANCE_ALREADY_PERFORMED,
          "A solicitação %d já possui a manutenção %d".formatted(id, maintenance.getId()));
    }
  }

  public void redirectTo(Redirect redirect, LocalDateTime dateTime) {
    Objects.requireNonNull(redirect, "Redirecionamento é obrigatório");
    Objects.requireNonNull(dateTime, "Data/hora do redirecionamento é obrigatória");
    Objects.requireNonNull(
        redirect.getSourceEmployee(), "Funcionário de origem do redirecionamento é obrigatório");
    Employee destinationEmployee =
        Objects.requireNonNull(
            redirect.getDestinationEmployee(), "Funcionário de destino é obrigatório");
    requireNotAssignedTo(destinationEmployee);

    transitionTo(MaintenanceRequestStatus.REDIRECIONADA, destinationEmployee, dateTime);
    this.employee = destinationEmployee;
    redirect.setCreatedAt(dateTime);
    addRedirect(redirect);
  }

  private void requireNotAssignedTo(Employee destinationEmployee) {
    if (employee != null && employee.getId().equals(destinationEmployee.getId())) {
      throw new AppException(
          ErrorCode.SELF_REDIRECT_NOT_ALLOWED,
          "A solicitação %d já é do funcionário %d".formatted(id, destinationEmployee.getId()));
    }
  }

  public void pay(LocalDateTime dateTime) {
    transitionTo(MaintenanceRequestStatus.PAGA, null, dateTime);
    this.paymentDate = dateTime;
  }

  public void finish(Employee employee, LocalDateTime dateTime) {
    Objects.requireNonNull(employee, "Funcionário que finaliza é obrigatório");

    transitionTo(MaintenanceRequestStatus.FINALIZADA, employee, dateTime);
    this.finalizedBy = employee;
    this.finalizedAt = dateTime;
  }

  private void transitionTo(
      MaintenanceRequestStatus target, @Nullable Employee employee, LocalDateTime dateTime) {
    transitionTo(target, employee, dateTime, null);
  }

  private void transitionTo(
      MaintenanceRequestStatus target,
      @Nullable Employee employee,
      LocalDateTime dateTime,
      @Nullable String reason) {
    Objects.requireNonNull(target, "Status de destino é obrigatório");
    Objects.requireNonNull(dateTime, "Data/hora da transição é obrigatória");

    if (!status.canTransitionTo(target)) {
      throw new AppException(
          ErrorCode.INVALID_STATUS_TRANSITION,
          "Transição inválida de %s para %s. A partir de %s só é possível ir para %s."
              .formatted(status, target, status, status.allowedTransitions()));
    }

    MaintenanceRequestHistory entry =
        new MaintenanceRequestHistory(target, dateTime, employee, reason);
    addHistory(entry);
  }

  private void requireOwner(Integer customerId) {
    if (!customer.getId().equals(customerId)) {
      throw new AppException(
          ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED,
          "A solicitação %d não pertence ao cliente %d".formatted(id, customerId));
    }
  }

  private void createHistory(LocalDateTime createdAt, @Nullable Employee employee) {
    this.history = new ArrayList<>();
    addHistory(new MaintenanceRequestHistory(MaintenanceRequestStatus.ABERTA, createdAt, employee));
  }

  private void addRedirect(Redirect redirect) {
    if (redirects == null) {
      redirects = new ArrayList<>();
    }
    if (redirects.contains(redirect)) {
      return;
    }
    redirects.add(redirect);
    redirect.setMaintenanceRequest(this);
  }

  private void addHistory(MaintenanceRequestHistory historyEntry) {
    if (history == null) {
      history = new ArrayList<>();
    }
    history.add(historyEntry);
    historyEntry.setMaintenanceRequest(this);
    if (historyEntry.getStatus() != null) {
      this.status = historyEntry.getStatus();
    }
    this.updatedAt = historyEntry.getUpdatedAt();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public String getDefect() {
    return defect;
  }

  public void setDefect(String defect) {
    this.defect = defect;
  }

  @Nullable
  public LocalDateTime getPaymentDate() {
    return paymentDate;
  }

  @Nullable
  public String getRejectionReason() {
    return rejectionReason;
  }

  @Nullable
  public LocalDateTime getFinalizedAt() {
    return finalizedAt;
  }

  @Nullable
  public Employee getFinalizedBy() {
    return finalizedBy;
  }

  public MaintenanceRequestStatus getStatus() {
    return status;
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public Customer getCustomer() {
    return customer;
  }

  @Nullable
  public Employee getEmployee() {
    return employee;
  }

  @Nullable
  public Budget getBudget() {
    return budget;
  }

  @Nullable
  public Maintenance getMaintenance() {
    return maintenance;
  }

  public List<MaintenanceRequestHistory> getHistory() {
    return history;
  }

  public List<Redirect> getRedirects() {
    return redirects != null ? redirects : List.of();
  }
}
