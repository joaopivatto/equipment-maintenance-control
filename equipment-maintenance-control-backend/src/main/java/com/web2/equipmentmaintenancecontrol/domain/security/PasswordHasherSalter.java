package com.web2.equipmentmaintenancecontrol.domain.security;

public interface PasswordHasherSalter {
  HashedSaltedPassword hashAndSalt(String rawPassword);

  boolean matches(String rawPassword, HashedSaltedPassword hashedSaltedPassword);
}
