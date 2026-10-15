package com.web2.equipmentmaintenancecontrol.model.profile;

import jakarta.persistence.*;
import java.util.Objects;

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

  public Customer() {}

  private Customer(Builder builder) {
    setName(Objects.requireNonNull(builder.name, "name é obrigatório"));
    setEmail(Objects.requireNonNull(builder.email, "email é obrigatório"));
    setPasswordHash(Objects.requireNonNull(builder.passwordHash, "passwordHash é obrigatório"));
    setPasswordSalt(Objects.requireNonNull(builder.passwordSalt, "passwordSalt é obrigatório"));
    setType(Objects.requireNonNull(builder.type, "type é obrigatório"));
    setActive(builder.active);
    this.cpf = Objects.requireNonNull(builder.cpf, "cpf é obrigatório");
    this.address = Objects.requireNonNull(builder.address, "address é obrigatório");
    this.phone = Objects.requireNonNull(builder.phone, "phone é obrigatório");
  }

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

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String name;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private ProfileType type;
    private boolean active = true;
    private String cpf;
    private Address address;
    private Phone phone;

    private Builder() {}

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder passwordHash(String passwordHash) {
      this.passwordHash = passwordHash;
      return this;
    }

    public Builder passwordSalt(String passwordSalt) {
      this.passwordSalt = passwordSalt;
      return this;
    }

    public Builder type(ProfileType type) {
      this.type = type;
      return this;
    }

    public Builder active(boolean active) {
      this.active = active;
      return this;
    }

    public Builder cpf(String cpf) {
      this.cpf = cpf;
      return this;
    }

    public Builder address(Address address) {
      this.address = address;
      return this;
    }

    public Builder phone(Phone phone) {
      this.phone = phone;
      return this;
    }

    public Customer build() {
      return new Customer(this);
    }
  }
}
