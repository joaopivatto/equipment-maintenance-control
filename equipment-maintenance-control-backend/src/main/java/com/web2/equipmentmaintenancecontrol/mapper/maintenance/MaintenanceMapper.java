package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Maintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceDetails;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper extends BaseMapper {

  private final EmployeeMapper employeeMapper;

  public MaintenanceMapper(EmployeeMapper employeeMapper) {
    this.employeeMapper = employeeMapper;
  }

  public MaintenanceDetails toDetails(Maintenance entity) {
    if (entity == null) {
      return null;
    }

    return new MaintenanceDetails(
        entity.getId(),
        entity.getDescription(),
        entity.getCustomerInstructions(),
        employeeMapper.toId(entity.getEmployee()),
        employeeMapper.toName(entity.getEmployee()));
  }
}
