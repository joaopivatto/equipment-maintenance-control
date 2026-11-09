package com.web2.equipmentmaintenancecontrol.domain.security;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

/** Gera senhas numéricas aleatórias usando {@link SecureRandom}. */
@Component
public class SecureRandomPasswordGenerator implements PasswordGenerator {
  private static final String NUMBERS = "0123456789";

  private final SecureRandom random = new SecureRandom();

  /** Gera uma senha com a quantidade de dígitos informada. */
  public String generate(int digits) {
    StringBuilder password = new StringBuilder(digits);
    for (int i = 0; i < digits; i++) {
      password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
    }
    return password.toString();
  }
}
