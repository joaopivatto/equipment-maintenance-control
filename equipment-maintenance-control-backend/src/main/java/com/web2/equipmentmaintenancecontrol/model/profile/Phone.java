package com.web2.equipmentmaintenancecontrol.model.profile;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Phone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String number;

  protected Phone() {}

  public Phone(String number) {
    this.number = Objects.requireNonNull(number, "number é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public String getNumber() {
    return number;
  }
}
