package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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

  public Maintenance() {}

  public Maintenance(
      Integer id,
      String description,
      String customerInstructions,
      Employee employee,
      LocalDateTime createdAt) {
    this.id = id;
    this.description = description;
    this.customerInstructions = customerInstructions;
    this.employee = employee;
    this.createdAt = createdAt;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCustomerInstructions() {
    return customerInstructions;
  }

  public void setCustomerInstructions(String customerInstructions) {
    this.customerInstructions = customerInstructions;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  private LocalDateTime createdAt;
}
