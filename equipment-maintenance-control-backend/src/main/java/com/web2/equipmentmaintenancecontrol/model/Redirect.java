package com.web2.equipmentmaintenancecontrol.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    private LocalDateTime createdAt;

    public Redirect () {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getSourceEmployee() {
        return sourceEmployee;
    }

    public void setSourceEmployee(Employee sourceEmployee) {
        this.sourceEmployee = sourceEmployee;
    }

    public Employee getDestinationEmployee() {
        return destinationEmployee;
    }

    public void setDestinationEmployee(Employee destinationEmployee) {
        this.destinationEmployee = destinationEmployee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
