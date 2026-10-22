package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Redirect {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "source_employee_id", nullable = false)
  private Employee sourceEmployee;

  @ManyToOne
  @JoinColumn(name = "destination_employee_id", nullable = false)
  private Employee destinationEmployee;

  @ManyToOne
  @JoinColumn(name = "maintenance_request_id", nullable = false)
  private MaintenanceRequest maintenanceRequest;

  private LocalDateTime createdAt;

  protected Redirect() {}

  public Redirect(Employee sourceEmployee, Employee destinationEmployee) {
    this.sourceEmployee = Objects.requireNonNull(sourceEmployee, "sourceEmployee é obrigatório");
    this.destinationEmployee =
        Objects.requireNonNull(destinationEmployee, "destinationEmployee é obrigatório");
  }

  void attachTo(MaintenanceRequest maintenanceRequest, LocalDateTime createdAt) {
    this.maintenanceRequest =
        Objects.requireNonNull(maintenanceRequest, "maintenanceRequest é obrigatório");
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public Employee getSourceEmployee() {
    return sourceEmployee;
  }

  public Employee getDestinationEmployee() {
    return destinationEmployee;
  }

  public MaintenanceRequest getMaintenanceRequest() {
    return maintenanceRequest;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
