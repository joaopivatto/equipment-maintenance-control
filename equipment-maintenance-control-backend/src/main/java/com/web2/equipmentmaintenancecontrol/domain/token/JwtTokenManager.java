package com.web2.equipmentmaintenancecontrol.domain.token;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager implements TokenManager {

  private static final String PROFILE_TYPE_CLAIM = "profileType";

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-ms}")
  private long expirationMs;

  public String generateToken(Integer userId, ProfileType profileType) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim(PROFILE_TYPE_CLAIM, profileType.name())
        .issuedAt(now)
        .expiration(expiry)
        .signWith(getSigningKey())
        .compact();
  }

  public void validateToken(String token) {
    try {
      parseClaims(token);
    } catch (JwtException | IllegalArgumentException e) {
      throw new AppException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
    }
  }

  public Integer extractUserId(String token) {
    return Integer.valueOf(parseClaims(token).getSubject());
  }

  public ProfileType extractProfileType(String token) {
    return ProfileType.valueOf(parseClaims(token).get(PROFILE_TYPE_CLAIM, String.class));
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
