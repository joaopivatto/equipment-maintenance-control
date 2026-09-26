package com.web2.equipmentmaintenancecontrol.controller.profile;

import com.web2.equipmentmaintenancecontrol.repository.profile.EmployeeRepository;
import com.web2.equipmentmaintenancecontrol.service.profile.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

}
