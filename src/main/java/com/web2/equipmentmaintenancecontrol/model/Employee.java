package com.web2.equipmentmaintenancecontrol.model;

import java.time.LocalDate;

public class Employee extends Profile{

    private String profileId;

    private LocalDate birthDate;


    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
