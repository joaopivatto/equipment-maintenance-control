package com.web2.equipmentmaintenancecontrol.model.profile;

import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Employee extends Profile {

  private LocalDate birthDate;

  public Employee() {}

  private Employee(Builder builder) {
    setName(Objects.requireNonNull(builder.name, "name é obrigatório"));
    setEmail(Objects.requireNonNull(builder.email, "email é obrigatório"));
    setPasswordHash(Objects.requireNonNull(builder.passwordHash, "passwordHash é obrigatório"));
    setPasswordSalt(Objects.requireNonNull(builder.passwordSalt, "passwordSalt é obrigatório"));
    setType(Objects.requireNonNull(builder.type, "type é obrigatório"));
    setActive(builder.active);
    this.birthDate = Objects.requireNonNull(builder.birthDate, "birthDate é obrigatório");
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
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
    private LocalDate birthDate;

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

    public Builder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
      return this;
    }

    public Employee build() {
      return new Employee(this);
    }
  }
}
