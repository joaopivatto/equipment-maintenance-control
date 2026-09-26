package com.web2.equipmentmaintenancecontrol.domain.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class Sha256PasswordHasherSalter implements PasswordHasherSalter {
  public HashedSaltedPassword hashAndSalt(String rawPassword) {
    byte[] saltBytes = generateSalt();
    byte[] hashBytes = sha256(rawPassword, saltBytes);

    String salt = Base64.getEncoder().encodeToString(saltBytes);
    String hash = Base64.getEncoder().encodeToString(hashBytes);

    return new HashedSaltedPassword(hash, salt);
  }

  public boolean matches(String rawPassword, HashedSaltedPassword hashedSaltedPassword) {
    byte[] saltBytes = Base64.getDecoder().decode(hashedSaltedPassword.salt());
    byte[] expectedHash = Base64.getDecoder().decode(hashedSaltedPassword.hash());

    byte[] actualHash = sha256(rawPassword, saltBytes);
    return MessageDigest.isEqual(expectedHash, actualHash);
  }

  private byte[] sha256(String password, byte[] salt) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(salt);
      return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }

  private byte[] generateSalt() {
    byte[] salt = new byte[16];
    new SecureRandom().nextBytes(salt);
    return salt;
  }
}
