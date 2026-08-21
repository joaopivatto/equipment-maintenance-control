package com.web2.equipmentmaintenancecontrol.repository;

import com.web2.equipmentmaintenancecontrol.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
