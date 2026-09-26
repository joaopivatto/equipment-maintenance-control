package com.web2.equipmentmaintenancecontrol.service.profile;

import com.web2.equipmentmaintenancecontrol.repository.profile.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }
}
