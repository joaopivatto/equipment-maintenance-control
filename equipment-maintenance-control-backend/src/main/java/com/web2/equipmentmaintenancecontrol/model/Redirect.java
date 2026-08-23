package com.web2.equipmentmaintenancecontrol.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Redirect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "souce_employee_id", nullable = false)
    private Integer sourceEmployeeId;

    @ManyToOne
    @JoinColumn(name = "destination_employee_id", nullable = false)
    private Integer destinationEmployeeId;

    private LocalDateTime createdAt;

    public Redirect () {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceEmployeeId() {
        return sourceEmployeeId;
    }

    public void setSourceEmployeeId(Integer sourceEmployeeId) {
        this.sourceEmployeeId = sourceEmployeeId;
    }

    public Integer getDestinationEmployeeId() {
        return destinationEmployeeId;
    }

    public void setDestinationEmployeeId(Integer destinationEmployeeId) {
        this.destinationEmployeeId = destinationEmployeeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
