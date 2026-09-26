package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.LocalDate;
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

  @Nullable private LocalDate paymentDate;

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

  @OneToOne
  @JoinColumn(name = "budget_id")
  @Nullable
  private Budget budget;

  @OneToOne
  @JoinColumn(name = "maintenance_id")
  @Nullable
  private Maintenance maintenance;

  @OneToMany(mappedBy = "maintenanceRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @OrderBy("updatedAt DESC")
  private List<MaintenanceRequestHistory> history;

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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getDefect() {
    return defect;
  }

  public void setDefect(String defect) {
    this.defect = defect;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public MaintenanceRequestStatus getStatus() {
    return status;
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Budget getBudget() {
    return budget;
  }

  public Maintenance getMaintenance() {
    return maintenance;
  }

  public void setMaintenance(Maintenance maintenance) {
    this.maintenance = maintenance;
  }

  public List<MaintenanceRequestHistory> getHistory() {
    return history;
  }

  public void createHistory(LocalDateTime createdAt, Employee employee) {
    this.history = new ArrayList<>();
    addHistory(new MaintenanceRequestHistory(MaintenanceRequestStatus.ABERTA, createdAt, employee));
  }

  public void addHistory(MaintenanceRequestHistory historyEntry) {
    history.add(historyEntry);
    historyEntry.setMaintenanceRequest(this);
    if (historyEntry.getStatus() != null) {
      this.status = historyEntry.getStatus();
    }
  }
}
