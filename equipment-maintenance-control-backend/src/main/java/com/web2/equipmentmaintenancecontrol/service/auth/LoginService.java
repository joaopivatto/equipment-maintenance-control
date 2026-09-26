package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.Profile;
import com.web2.equipmentmaintenancecontrol.repository.profile.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private final ProfileRepository repository;
  private final PasswordHasherSalter passwordHasherSalter;

  LoginService(ProfileRepository repository, PasswordHasherSalter passwordHasherSalter) {
    this.repository = repository;
    this.passwordHasherSalter = passwordHasherSalter;
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

    return new LoginResponse(profile.getType(), profile.getId());
  }
}
