package com.web2.equipmentmaintenancecontrol.domain.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sha256PasswordHasherSalter")
class Sha256PasswordHasherSalterTest {

  private final Sha256PasswordHasherSalter hasher = new Sha256PasswordHasherSalter();

  @Test
  @DisplayName("gera hash e salt em Base64 sem expor a senha original")
  void hashAndSaltGeneratesBase64() {
    HashedSaltedPassword result = hasher.hashAndSalt("1234");

    assertThat(result.hash()).isNotBlank().isNotEqualTo("1234");
    assertThat(result.salt()).isNotBlank();
    assertThat(Base64.getDecoder().decode(result.salt())).hasSize(16);
    assertThat(Base64.getDecoder().decode(result.hash())).hasSize(32);
  }

  @Test
  @DisplayName("gera salts distintos para a mesma senha")
  void hashAndSaltGeneratesDistinctSalts() {
    HashedSaltedPassword first = hasher.hashAndSalt("1234");
    HashedSaltedPassword second = hasher.hashAndSalt("1234");

    assertThat(first.salt()).isNotEqualTo(second.salt());
    assertThat(first.hash()).isNotEqualTo(second.hash());
  }

  @Test
  @DisplayName("reconhece a senha correta")
  void matchesCorrectPassword() {
    HashedSaltedPassword stored = hasher.hashAndSalt("1234");

    assertThat(hasher.matches("1234", stored)).isTrue();
  }

  @Test
  @DisplayName("rejeita a senha incorreta")
  void doesNotMatchWrongPassword() {
    HashedSaltedPassword stored = hasher.hashAndSalt("1234");

    assertThat(hasher.matches("4321", stored)).isFalse();
    assertThat(hasher.matches("", stored)).isFalse();
  }

  @Test
  @DisplayName("rejeita a senha quando o salt armazenado é de outro registro")
  void doesNotMatchWhenSaltDiffers() {
    HashedSaltedPassword stored = hasher.hashAndSalt("1234");
    HashedSaltedPassword other = hasher.hashAndSalt("1234");

    HashedSaltedPassword mixed = new HashedSaltedPassword(stored.hash(), other.salt());

    assertThat(hasher.matches("1234", mixed)).isFalse();
  }
}
