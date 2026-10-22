package com.web2.equipmentmaintenancecontrol.model.profile;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String zipCode;

  private String street;

  private Integer number;

  private String complement;

  private String neighborhood;

  private String city;

  private String state;

  protected Address() {}

  public Address(
      String zipCode,
      String street,
      Integer number,
      String complement,
      String neighborhood,
      String city,
      String state) {
    this.zipCode = Objects.requireNonNull(zipCode, "zipCode é obrigatório");
    this.street = Objects.requireNonNull(street, "street é obrigatório");
    this.number = number;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.city = city;
    this.state = state;
  }

  public Integer getId() {
    return id;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getStreet() {
    return street;
  }

  public Integer getNumber() {
    return number;
  }

  public String getComplement() {
    return complement;
  }

  public String getNeighborhood() {
    return neighborhood;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }
}
