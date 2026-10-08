package com.web2.equipmentmaintenancecontrol.service.profile;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
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

  public EmployeeService(EmployeeRepository repository) {
    this.repository = repository;
  }

  public EmployeeResponse findById(Integer id) {
    return toResponse(this.findEntityById(id));
  }

  public List<EmployeeResponse> findAll() {
    return this.repository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional
  public EmployeeResponse create(EmployeeCreateRequest request) {

    Employee employee = new Employee();
    employee.setName(request.name().trim());
    employee.setEmail(request.email());
    employee.setBirthDate(request.birthDate());
    employee.setType(ProfileType.EMPLOYEE);
    employee.setActive(true);

    Employee savedEmployee = repository.save(employee);

    return toResponse(savedEmployee);
  }

  @Transactional
  public EmployeeResponse update(Integer id, EmployeeUpdateRequest request) {
    Employee employee = findEntityById(id);

    employee.setName(request.name().trim());
    employee.setEmail(request.email());
    employee.setBirthDate(request.birthDate());

    Employee updatedEmployee = repository.save(employee);

    return toResponse(updatedEmployee);
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

  private EmployeeResponse toResponse(Employee employee) {
    return new EmployeeResponse(
        employee.getId(),
        employee.getName(),
        employee.getEmail(),
        employee.getBirthDate(),
        employee.getActive());
  }

  private Employee findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
  }
}
