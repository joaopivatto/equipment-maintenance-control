package com.web2.equipmentmaintenancecontrol.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final String[] allowedOrigins;
  private final TokenInterceptor sessionInterceptor;

  public WebConfig(
      @Value("${app.cors.allowed-origins:http://localhost:4200}") String[] allowedOrigins,
      TokenInterceptor sessionInterceptor) {
    this.allowedOrigins = allowedOrigins;
    this.sessionInterceptor = sessionInterceptor;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(sessionInterceptor).excludePathPatterns("/auth/login", "/auth/signup");
  }
}
