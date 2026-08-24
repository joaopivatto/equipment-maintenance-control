package com.web2.equipmentmaintenancecontrol.model;

import com.web2.equipmentmaintenancecontrol.enums.MaintenanceRequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  public MaintenanceRequestHistory() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public MaintenanceRequest getMaintenanceRequest() {
    return maintenanceRequest;
  }

  public void setMaintenanceRequest(MaintenanceRequest maintenanceRequest) {
    this.maintenanceRequest = maintenanceRequest;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public MaintenanceRequestStatus getStatus() {
    return status;
  }

  public void setStatus(MaintenanceRequestStatus status) {
    this.status = status;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
}
