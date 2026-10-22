package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Budget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private BigDecimal value;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  private LocalDateTime createdAt;

  protected Budget() {}

  public Budget(BigDecimal value, Employee employee, LocalDateTime createdAt) {
    this.value = Objects.requireNonNull(value, "value é obrigatório");
    this.employee = Objects.requireNonNull(employee, "employee é obrigatório");
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public BigDecimal getValue() {
    return value;
  }

  public Employee getEmployee() {
    return employee;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
