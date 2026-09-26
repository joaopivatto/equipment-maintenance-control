package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestHistory;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceRequestMapper extends BaseMapper {

  private final EquipmentMapper equipmentMapper;
  private final BudgetMapper budgetMapper;
  private final MaintenanceMapper maintenanceMapper;
  private final MaintenanceRequestHistoryMapper historyMapper;
  private final EmployeeMapper employeeMapper;

  public MaintenanceRequestMapper(
      EquipmentMapper equipmentMapper,
      BudgetMapper budgetMapper,
      MaintenanceMapper maintenanceMapper,
      MaintenanceRequestHistoryMapper historyMapper,
      EmployeeMapper employeeMapper) {
    this.equipmentMapper = equipmentMapper;
    this.budgetMapper = budgetMapper;
    this.maintenanceMapper = maintenanceMapper;
    this.historyMapper = historyMapper;
    this.employeeMapper = employeeMapper;
  }

  public MaintenanceRequestDetails toDetails(MaintenanceRequest entity) {
    if (entity == null) {
      return null;
    }

    MaintenanceRequestHistory finalizedEntry =
        findLastEntryByStatus(entity, MaintenanceRequestStatus.FINALIZADA);

    return new MaintenanceRequestDetails(
        entity.getId(),
        formatDateTime(entity.getCreatedAt()),
        equipmentMapper.toDescription(entity.getEquipment()),
        equipmentMapper.toCategoryName(entity.getEquipment()),
        entity.getDefect(),
        entity.getStatus(),
        budgetMapper.toDetails(entity.getBudget()),
        null,
        entity.getCustomer().getName(),
        maintenanceMapper.toDetails(entity.getMaintenance()),
        formatDate(entity.getPaymentDate()),
        finalizedEntry != null ? formatDateTime(finalizedEntry.getUpdatedAt()) : null,
        finalizedEntry != null ? employeeMapper.toName(finalizedEntry.getEmployee()) : null,
        historyMapper.toDetails(entity.getHistory()));
  }

  public List<MaintenanceRequestDetails> toDetails(List<MaintenanceRequest> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toDetails).toList();
  }

  private MaintenanceRequestHistory findLastEntryByStatus(
      MaintenanceRequest entity, MaintenanceRequestStatus status) {
    if (entity.getHistory() == null) {
      return null;
    }
    return entity.getHistory().stream()
        .filter(entry -> entry.getStatus() == status)
        .findFirst()
        .orElse(null);
  }
}
