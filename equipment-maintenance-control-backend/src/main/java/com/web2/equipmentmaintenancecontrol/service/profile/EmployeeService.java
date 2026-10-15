package com.web2.equipmentmaintenancecontrol.service.profile;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeCreateRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeUpdateRequest;
import com.web2.equipmentmaintenancecontrol.repository.profile.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

  private final EmployeeRepository repository;
  private final EmployeeMapper mapper;
  private final PasswordHasherSalter passwordHasherSalter;

  public EmployeeService(
      EmployeeRepository repository,
      EmployeeMapper mapper,
      PasswordHasherSalter passwordHasherSalter) {
    this.repository = repository;
    this.mapper = mapper;
    this.passwordHasherSalter = passwordHasherSalter;
  }

  public EmployeeResponse findById(Integer id) {
    return mapper.toResponse(this.findEntityById(id));
  }

  public List<EmployeeResponse> findAll() {
    return mapper.toResponse(this.repository.findAll());
  }

  @Transactional
  public EmployeeResponse create(EmployeeCreateRequest request) {

    HashedSaltedPassword hashedSaltedPassword =
        passwordHasherSalter.hashAndSalt(request.password());

    Employee employee =
        Employee.builder()
            .name(request.name())
            .email(request.email())
            .birthDate(request.birthDate())
            .type(ProfileType.EMPLOYEE)
            .passwordHash(hashedSaltedPassword.hash())
            .passwordSalt(hashedSaltedPassword.salt())
            .build();

    Employee savedEmployee = repository.save(employee);

    return mapper.toResponse(savedEmployee);
  }

  @Transactional
  public EmployeeResponse update(Integer id, EmployeeUpdateRequest request) {
    Employee employee = findEntityById(id);

    employee.setName(request.name());
    employee.setEmail(request.email());
    employee.setBirthDate(request.birthDate());

    Employee updatedEmployee = repository.save(employee);

    return mapper.toResponse(updatedEmployee);
  }

  @Transactional
  public void delete(Integer id, Integer currentEmployeeId) {
    Employee employee = findEntityById(id);

    if (id.equals(currentEmployeeId)) {
      throw new AppException(ErrorCode.EMPLOYEE_CANNOT_DEACTIVATE_SELF);
    }

    if (!employee.getActive()) {
      throw new AppException(ErrorCode.EMPLOYEE_ALREADY_INACTIVE);
    }

    if (repository.countByActiveTrue() <= 1) {
      throw new AppException(ErrorCode.EMPLOYEE_LAST_ACTIVE);
    }

    repository.delete(employee);
  }

  public Employee findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
  }
}
