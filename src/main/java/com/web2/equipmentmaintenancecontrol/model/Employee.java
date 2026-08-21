package com.web2.equipmentmaintenancecontrol.model;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Employee extends Profile{

    private LocalDate birthDate;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
