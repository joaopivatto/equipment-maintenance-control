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

  public TokenClaims parseToken(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

      Integer userId = Integer.valueOf(claims.getSubject());
      ProfileType profileType = ProfileType.valueOf(claims.get(PROFILE_TYPE_CLAIM, String.class));

      return new TokenClaims(userId, profileType);
    } catch (JwtException | IllegalArgumentException e) {
      throw new AppException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
    }
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
