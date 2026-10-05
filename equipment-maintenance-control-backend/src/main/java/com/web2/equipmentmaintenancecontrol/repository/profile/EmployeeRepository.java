package com.web2.equipmentmaintenancecontrol.repository.profile;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.repository.ActiveRepository;

public interface EmployeeRepository extends ActiveRepository<Employee, Integer> {

  long countByActiveTrue();
}
