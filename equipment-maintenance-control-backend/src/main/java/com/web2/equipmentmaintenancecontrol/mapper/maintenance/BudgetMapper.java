package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Budget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.BudgetDetails;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper extends BaseMapper {

  private final EmployeeMapper employeeMapper;

  public BudgetMapper(EmployeeMapper employeeMapper) {
    this.employeeMapper = employeeMapper;
  }

  public BudgetDetails toDetails(Budget entity) {
    if (entity == null) {
      return null;
    }

    return new BudgetDetails(
        entity.getId(),
        entity.getValue(),
        formatDateTime(entity.getCreatedAt()),
        employeeMapper.toId(entity.getEmployee()));
  }
}
