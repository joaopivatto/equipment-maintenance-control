package com.web2.equipmentmaintenancecontrol.domain.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordEmail {
  @Value("${email.provider.sender}")
  private String sender;

  public Email build(String recipient, String rawPassword) {
    String subject = "Olá! Sua senha de acesso foi gerada";
    String body = "<p>Sua senha de acesso é: <strong>" + rawPassword + "</strong></p>";

    return new Email(sender, recipient, subject, body);
  }
}
