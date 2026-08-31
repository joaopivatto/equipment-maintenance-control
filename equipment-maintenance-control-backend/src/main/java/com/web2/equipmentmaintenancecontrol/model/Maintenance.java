package com.web2.equipmentmaintenancecontrol.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }

  private LocalDate createdAt;
}
