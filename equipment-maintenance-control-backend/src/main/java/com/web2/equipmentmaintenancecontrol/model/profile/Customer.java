package com.web2.equipmentmaintenancecontrol.model.profile;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Customer extends Profile {

  @Column(unique = true, nullable = false, length = 11)
  private String cpf;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "phone_id")
  private Phone phone;

  protected Customer() {}

  public Customer(
      String name,
      String email,
      HashedSaltedPassword password,
      String cpf,
      Address address,
      Phone phone) {
    super(name, email, password, ProfileType.CUSTOMER);
    this.cpf = Objects.requireNonNull(cpf, "cpf é obrigatório");
    this.address = Objects.requireNonNull(address, "address é obrigatório");
    this.phone = Objects.requireNonNull(phone, "phone é obrigatório");
  }

  public String getCpf() {
    return cpf;
  }

  public Address getAddress() {
    return address;
  }

  public Phone getPhone() {
    return phone;
  }
}
