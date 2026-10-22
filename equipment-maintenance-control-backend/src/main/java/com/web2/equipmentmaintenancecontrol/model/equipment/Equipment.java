package com.web2.equipmentmaintenancecontrol.model.equipment;

import com.web2.equipmentmaintenancecontrol.model.ActivatableEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Equipment extends ActivatableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String description;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private EquipmentType type;

  protected Equipment() {}

  public Equipment(String description, EquipmentType type) {
    this.description = requireDescription(description);
    this.type = requireType(type);
  }

  public void update(String description, EquipmentType type) {
    this.description = requireDescription(description);
    this.type = requireType(type);
  }

  private static String requireDescription(String description) {
    return Objects.requireNonNull(description, "description é obrigatório");
  }

  private static EquipmentType requireType(EquipmentType type) {
    return Objects.requireNonNull(type, "type é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public EquipmentType getType() {
    return type;
  }
}
