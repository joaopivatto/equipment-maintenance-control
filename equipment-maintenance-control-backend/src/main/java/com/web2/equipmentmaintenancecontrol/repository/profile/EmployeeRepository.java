package com.web2.equipmentmaintenancecontrol.repository.profile;

import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {}
