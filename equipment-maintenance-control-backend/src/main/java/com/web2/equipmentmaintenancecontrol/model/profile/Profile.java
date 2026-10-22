package com.web2.equipmentmaintenancecontrol.model.profile;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.model.ActivatableEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile extends ActivatableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  private String passwordHash;

  private String passwordSalt;

  @Enumerated(EnumType.STRING)
  private ProfileType type;

  protected Profile() {}

  protected Profile(String name, String email, HashedSaltedPassword password, ProfileType type) {
    this.name = requireName(name);
    this.email = requireEmail(email);
    Objects.requireNonNull(password, "password é obrigatório");
    this.passwordHash = Objects.requireNonNull(password.hash(), "passwordHash é obrigatório");
    this.passwordSalt = Objects.requireNonNull(password.salt(), "passwordSalt é obrigatório");
    this.type = Objects.requireNonNull(type, "type é obrigatório");
  }

  protected void updateBasicInfo(String name, String email) {
    this.name = requireName(name);
    this.email = requireEmail(email);
  }

  private static String requireName(String name) {
    return Objects.requireNonNull(name, "name é obrigatório");
  }

  private static String requireEmail(String email) {
    return Objects.requireNonNull(email, "email é obrigatório");
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public ProfileType getType() {
    return type;
  }
}
