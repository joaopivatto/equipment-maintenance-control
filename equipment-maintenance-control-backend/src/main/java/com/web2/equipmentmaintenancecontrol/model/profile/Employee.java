package com.web2.equipmentmaintenancecontrol.model.profile;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Employee extends Profile {

  private LocalDate birthDate;

  protected Employee() {}

  public Employee(String name, String email, LocalDate birthDate, HashedSaltedPassword password) {
    super(name, email, password, ProfileType.EMPLOYEE);
    this.birthDate = requireBirthDate(birthDate);
  }

  public void update(String name, String email, LocalDate birthDate) {
    updateBasicInfo(name, email);
    this.birthDate = requireBirthDate(birthDate);
  }

  private static LocalDate requireBirthDate(LocalDate birthDate) {
    return Objects.requireNonNull(birthDate, "birthDate é obrigatório");
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }
}
