package com.web2.equipmentmaintenancecontrol.model.equipment;

import com.web2.equipmentmaintenancecontrol.model.ActivatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class EquipmentType extends ActivatableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String description;

  protected EquipmentType() {}

  public EquipmentType(String description) {
    this.description = requireDescription(description);
  }

  public void updateDescription(String description) {
    this.description = requireDescription(description);
  }

  private static String requireDescription(String description) {
    return Objects.requireNonNull(description, "description é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }
}
