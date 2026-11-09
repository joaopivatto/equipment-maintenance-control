package com.web2.equipmentmaintenancecontrol.domain.security;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

/**
 * Implementação de hash de senha usando SHA-256 com salt aleatório de 16 bytes. Hash e salt são
 * armazenados em Base64.
 */
@Component
public class Sha256PasswordHasherSalter implements PasswordHasherSalter {
  /** Gera um novo salt e calcula o hash da senha informada. */
  public HashedSaltedPassword hashAndSalt(String rawPassword) {
    byte[] saltBytes = generateSalt();
    byte[] hashBytes = sha256(rawPassword, saltBytes);

    String salt = Base64.getEncoder().encodeToString(saltBytes);
    String hash = Base64.getEncoder().encodeToString(hashBytes);

    return new HashedSaltedPassword(hash, salt);
  }

  /**
   * Recalcula o hash da senha com o salt armazenado e compara em tempo constante para evitar
   * ataques de temporização.
   */
  public boolean matches(String rawPassword, HashedSaltedPassword hashedSaltedPassword) {
    byte[] saltBytes = Base64.getDecoder().decode(hashedSaltedPassword.salt());
    byte[] expectedHash = Base64.getDecoder().decode(hashedSaltedPassword.hash());

    byte[] actualHash = sha256(rawPassword, saltBytes);
    return MessageDigest.isEqual(expectedHash, actualHash);
  }

  /** Aplica SHA-256 sobre o salt seguido da senha em UTF-8. */
  private byte[] sha256(String password, byte[] salt) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(salt);
      return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new AppException(ErrorCode.INTERNAL_ERROR);
    }
  }

  /** Gera um salt criptograficamente seguro. */
  private byte[] generateSalt() {
    byte[] salt = new byte[16];
    new SecureRandom().nextBytes(salt);
    return salt;
  }
}
