package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestHistory;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.HistoryEntryDetails;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceRequestHistoryMapper extends BaseMapper {

  private final EmployeeMapper employeeMapper;

  public MaintenanceRequestHistoryMapper(EmployeeMapper employeeMapper) {
    this.employeeMapper = employeeMapper;
  }

  public HistoryEntryDetails toDetails(MaintenanceRequestHistory entity) {
    if (entity == null) {
      return null;
    }

    return new HistoryEntryDetails(
        entity.getStatus(),
        formatDateTime(entity.getUpdatedAt()),
        employeeMapper.toName(entity.getEmployee()),
        null,
        null,
        null);
  }

  public List<HistoryEntryDetails> toDetails(List<MaintenanceRequestHistory> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toDetails).toList();
  }
}
