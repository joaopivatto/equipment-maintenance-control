package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Maintenance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String description;

  private String customerInstructions;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  private LocalDateTime createdAt;

  protected Maintenance() {}

  public Maintenance(
      String description, String customerInstructions, Employee employee, LocalDateTime createdAt) {
    this.description = Objects.requireNonNull(description, "description é obrigatório");
    this.customerInstructions = customerInstructions;
    this.employee = Objects.requireNonNull(employee, "employee é obrigatório");
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getCustomerInstructions() {
    return customerInstructions;
  }

  public Employee getEmployee() {
    return employee;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
