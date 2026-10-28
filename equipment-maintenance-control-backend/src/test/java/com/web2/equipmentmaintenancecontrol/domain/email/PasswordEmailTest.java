package com.web2.equipmentmaintenancecontrol.domain.email;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("PasswordEmail")
class PasswordEmailTest {

  @Test
  @DisplayName("monta e-mail com remetente configurado, destinatário e senha no corpo")
  void buildsPasswordEmail() {
    PasswordEmail passwordEmail = new PasswordEmail();
    ReflectionTestUtils.setField(passwordEmail, "sender", "no-reply@web2.com");

    Email email = passwordEmail.build("cliente@web2.com", "1234");

    assertThat(email.sender()).isEqualTo("no-reply@web2.com");
    assertThat(email.recipient()).isEqualTo("cliente@web2.com");
    assertThat(email.subject()).isEqualTo("Olá! Sua senha de acesso foi gerada");
    assertThat(email.body()).contains("1234").contains("<strong>");
  }
}
