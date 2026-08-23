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
    private Integer maintenanceRequestId;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private MaintenanceRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Integer employeeId;

    public MaintenanceRequestHistory() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaintenanceRequestId() {
        return maintenanceRequestId;
    }

    public void setMaintenanceRequestId(Integer maintenanceRequestId) {
        this.maintenanceRequestId = maintenanceRequestId;
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

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
