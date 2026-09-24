package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  public LoginResponse execute(LoginRequest request) {
    return new LoginResponse(true);
  }
}
