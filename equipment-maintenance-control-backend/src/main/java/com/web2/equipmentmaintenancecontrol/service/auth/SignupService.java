package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.SignupRequest;
import com.web2.equipmentmaintenancecontrol.repository.profile.CustomerRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class SignupService extends BaseService {
  private final CustomerRepository repository;

  SignupService(CustomerRepository repository) {
    this.repository = repository;
  }

  public void execute(SignupRequest request) {
    this.validateRequest(request);
  }

  private void validateRequest(SignupRequest request) {
    if (!isValidCpf(request)) {
      throw new AppException(ErrorCode.INVALID_CPF);
    }

    if (userAlreadyExists(request)) {
      throw new AppException(ErrorCode.CUSTOMER_ALREADY_EXISTS);
    }

    if (emailAlreadyExists(request)) {
      throw new AppException(ErrorCode.CUSTOMER_EMAIL_ALREADY_EXISTS);
    }
  }

  private boolean emailAlreadyExists(SignupRequest request) {
    return repository.findByEmail(request.cpf()) != null;
  }

  private boolean userAlreadyExists(SignupRequest request) {
    return repository.findByCpf(request.cpf()) != null;
  }

  private boolean isValidCpf(SignupRequest request) {
    return true;
  }
}
