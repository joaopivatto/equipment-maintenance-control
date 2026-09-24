package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  public LoginResponse execute(LoginRequest request) {
    boolean isLogged = true;

    if (!isLogged) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    return new LoginResponse(isLogged);
  }
}
