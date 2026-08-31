package com.web2.equipmentmaintenancecontrol.model;

import jakarta.persistence.*;

@Entity
public class Customer extends Profile {

  @Column(unique = true, nullable = false, length = 11)
  private String cpf;

  @OneToOne
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToOne
  @JoinColumn(name = "phone_id")
  private Phone phone;

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Phone getPhone() {
    return phone;
  }

  public void setPhone(Phone phone) {
    this.phone = phone;
  }
}
