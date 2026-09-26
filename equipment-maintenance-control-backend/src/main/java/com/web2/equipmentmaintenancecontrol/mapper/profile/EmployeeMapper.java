package com.web2.equipmentmaintenancecontrol.mapper.profile;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper extends BaseMapper {

  public Integer toId(Employee employee) {
    return employee != null ? employee.getId() : null;
  }

  public String toName(Employee employee) {
    return employee != null ? employee.getName() : null;
  }
}
