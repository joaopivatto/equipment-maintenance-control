package com.web2.equipmentmaintenancecontrol.controller.auth;

import com.web2.equipmentmaintenancecontrol.model.auth.dto.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.LoginResponse;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.SignupRequest;
import com.web2.equipmentmaintenancecontrol.service.auth.LoginService;
import com.web2.equipmentmaintenancecontrol.service.auth.SignupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final LoginService loginService;
  private final SignupService signupService;

  public AuthController(LoginService loginService, SignupService signupService) {
    this.loginService = loginService;
    this.signupService = signupService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(loginService.execute(request));
  }

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
    signupService.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
