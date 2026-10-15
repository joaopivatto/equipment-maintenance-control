package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.domain.token.TokenManager;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.LoginResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.Profile;
import com.web2.equipmentmaintenancecontrol.repository.profile.ProfileRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends BaseService {

  private final ProfileRepository repository;
  private final PasswordHasherSalter passwordHasherSalter;
  private final TokenManager tokenManager;

  LoginService(
      ProfileRepository repository,
      PasswordHasherSalter passwordHasherSalter,
      TokenManager tokenManager) {
    this.repository = repository;
    this.passwordHasherSalter = passwordHasherSalter;
    this.tokenManager = tokenManager;
  }

  public LoginResponse execute(LoginRequest request) {
    Profile profile = this.repository.findByEmail(request.email());

    if (profile == null) throw new AppException(ErrorCode.INVALID_CREDENTIALS);

    HashedSaltedPassword hashedSaltedPassword =
        new HashedSaltedPassword(profile.getPasswordHash(), profile.getPasswordSalt());
    boolean matches = this.passwordHasherSalter.matches(request.password(), hashedSaltedPassword);

    if (!matches) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    String token = tokenManager.generateToken(profile.getId(), profile.getType());

    return new LoginResponse(profile.getType(), profile.getId(), token);
  }
}
