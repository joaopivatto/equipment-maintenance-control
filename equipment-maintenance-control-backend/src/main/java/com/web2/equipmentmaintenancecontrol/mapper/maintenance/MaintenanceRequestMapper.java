package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestHistory;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
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
    MaintenanceRequestHistory rejectedEntry =
        findLastEntryByStatus(entity, MaintenanceRequestStatus.REJEITADA);

    Customer customer = entity.getCustomer();

    return new MaintenanceRequestDetails(
        entity.getId(),
        formatDateTime(entity.getCreatedAt()),
        formatDateTime(entity.getUpdatedAt()),
        equipmentMapper.toId(entity.getEquipment()),
        equipmentMapper.toDescription(entity.getEquipment()),
        equipmentMapper.toCategoryId(entity.getEquipment()),
        equipmentMapper.toCategoryName(entity.getEquipment()),
        entity.getDefect(),
        entity.getStatus(),
        budgetMapper.toDetails(entity.getBudget()),
        resolveRejectionReason(entity, rejectedEntry),
        customer != null ? customer.getId() : null,
        customer != null ? customer.getName() : null,
        customer != null ? customer.getEmail() : null,
        employeeMapper.toId(entity.getEmployee()),
        employeeMapper.toName(entity.getEmployee()),
        maintenanceMapper.toDetails(entity.getMaintenance()),
        formatDateTime(entity.getPaymentDate()),
        resolveFinalizedAt(entity, finalizedEntry),
        resolveFinalizedBy(entity, finalizedEntry),
        historyMapper.toDetails(entity.getHistory()));
  }

  public List<MaintenanceRequestDetails> toDetails(List<MaintenanceRequest> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toDetails).toList();
  }

  private String resolveFinalizedAt(
      MaintenanceRequest entity, MaintenanceRequestHistory finalizedEntry) {
    if (entity.getFinalizedAt() != null) {
      return formatDateTime(entity.getFinalizedAt());
    }
    return finalizedEntry != null ? formatDateTime(finalizedEntry.getUpdatedAt()) : null;
  }

  private String resolveFinalizedBy(
      MaintenanceRequest entity, MaintenanceRequestHistory finalizedEntry) {
    if (entity.getFinalizedBy() != null) {
      return employeeMapper.toName(entity.getFinalizedBy());
    }
    return finalizedEntry != null ? employeeMapper.toName(finalizedEntry.getEmployee()) : null;
  }

  private String resolveRejectionReason(
      MaintenanceRequest entity, MaintenanceRequestHistory rejectedEntry) {
    if (entity.getRejectionReason() != null) {
      return entity.getRejectionReason();
    }
    return rejectedEntry != null ? rejectedEntry.getReason() : null;
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
