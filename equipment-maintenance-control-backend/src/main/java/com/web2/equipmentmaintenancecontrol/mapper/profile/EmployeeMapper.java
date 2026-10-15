package com.web2.equipmentmaintenancecontrol.mapper.profile;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper extends BaseMapper {

  public EmployeeResponse toResponse(Employee entity) {
    if (entity == null) {
      return null;
    }
    return new EmployeeResponse(
        entity.getId(), entity.getName(), entity.getEmail(), entity.getBirthDate());
  }

  public List<EmployeeResponse> toResponse(List<Employee> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toResponse).toList();
  }
}
