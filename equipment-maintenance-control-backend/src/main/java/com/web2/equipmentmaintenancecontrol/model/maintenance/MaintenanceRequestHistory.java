package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class MaintenanceRequestHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "maintenance_request_id", nullable = false)
  private MaintenanceRequest maintenanceRequest;

  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  private MaintenanceRequestStatus status;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  @Nullable
  private Employee employee;

  @Nullable private String reason;

  protected MaintenanceRequestHistory() {}

  public MaintenanceRequestHistory(MaintenanceRequestStatus status, LocalDateTime updatedAt) {
    this(status, updatedAt, null);
  }

  public MaintenanceRequestHistory(
      MaintenanceRequestStatus status, LocalDateTime updatedAt, @Nullable Employee employee) {
    this(status, updatedAt, employee, null);
  }

  public MaintenanceRequestHistory(
      MaintenanceRequestStatus status,
      LocalDateTime updatedAt,
      @Nullable Employee employee,
      @Nullable String reason) {
    this.status = Objects.requireNonNull(status, "status é obrigatório");
    this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt é obrigatório");
    this.employee = employee;
    this.reason = reason;
  }

  void attachTo(MaintenanceRequest maintenanceRequest) {
    this.maintenanceRequest =
        Objects.requireNonNull(maintenanceRequest, "maintenanceRequest é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public MaintenanceRequest getMaintenanceRequest() {
    return maintenanceRequest;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public MaintenanceRequestStatus getStatus() {
    return status;
  }

  @Nullable
  public Employee getEmployee() {
    return employee;
  }

  @Nullable
  public String getReason() {
    return reason;
  }
}
