package com.web2.equipmentmaintenancecontrol.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ActivatableEntity {

  @Column(nullable = false)
  private boolean active = true;

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
