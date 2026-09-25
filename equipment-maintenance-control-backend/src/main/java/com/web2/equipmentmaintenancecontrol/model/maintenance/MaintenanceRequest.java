package com.web2.equipmentmaintenancecontrol.model.maintenance;

import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MaintenanceRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String defect;

  private LocalDate paymentDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MaintenanceRequestStatus status = MaintenanceRequestStatus.ABERTA;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @OneToOne
  @JoinColumn(name = "budget_id")
  private Budget budget;

  @OneToOne
  @JoinColumn(name = "maintenance_id")
  private Maintenance maintenance;

  @OneToMany(mappedBy = "maintenanceRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @OrderBy("updatedAt DESC")
  private List<MaintenanceRequestHistory> history = new ArrayList<>();

  public MaintenanceRequest() {}

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

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  public MaintenanceRequestStatus getStatus() {
    return status;
  }

  public void setStatus(MaintenanceRequestStatus status) {
    this.status = status;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
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

  public void setBudget(Budget budget) {
    this.budget = budget;
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

  public void addHistory(MaintenanceRequestHistory historyEntry) {
    history.add(historyEntry);
    historyEntry.setMaintenanceRequest(this);
    if (historyEntry.getStatus() != null) {
      this.status = historyEntry.getStatus();
    }
  }

  public void removeHistory(MaintenanceRequestHistory historyEntry) {
    history.remove(historyEntry);
    historyEntry.setMaintenanceRequest(null);
  }
}
