package com.web2.equipmentmaintenancecontrol.domain.security;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class SecureRandomPasswordGenerator implements PasswordGenerator {
  private static final String NUMBERS = "0123456789";

  private final SecureRandom random = new SecureRandom();

  public String generate(int digits) {
    StringBuilder password = new StringBuilder(digits);
    for (int i = 0; i < digits; i++) {
      password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
    }
    return password.toString();
  }
}
