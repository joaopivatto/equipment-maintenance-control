package com.web2.equipmentmaintenancecontrol.config;

import com.web2.equipmentmaintenancecontrol.domain.token.TokenClaims;
import com.web2.equipmentmaintenancecontrol.domain.token.TokenManager;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  public static final String USER_ID_ATTRIBUTE = "userId";
  public static final String PROFILE_TYPE_ATTRIBUTE = "profileType";

  private final TokenManager tokenManager;

  public TokenInterceptor(TokenManager tokenManager) {
    this.tokenManager = tokenManager;
  }

  public boolean preHandle(
      HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
    if (HttpMethod.OPTIONS.matches(request.getMethod())) {
      return true;
    }

    String token = extractToken(request);
    TokenClaims claims = tokenManager.parseToken(token);

    request.setAttribute(USER_ID_ATTRIBUTE, claims.userId());
    request.setAttribute(PROFILE_TYPE_ATTRIBUTE, claims.profileType());

    return true;
  }

  private String extractToken(HttpServletRequest request) {
    String header = request.getHeader(AUTHORIZATION_HEADER);

    if (header == null || !header.startsWith(BEARER_PREFIX)) {
      throw new AppException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
    }

    return header.substring(BEARER_PREFIX.length());
  }
}
