package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Budget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceRequestMapper extends BaseMapper {

  public MaintenanceRequestDetails toDetails(MaintenanceRequest entity) {
    if (entity == null) {
      return null;
    }

    Long id = entity.getId() != null ? entity.getId().longValue() : null;

    MaintenanceRequestStatus status =
        entity.getStatus() != null ? entity.getStatus() : MaintenanceRequestStatus.ABERTA;

    Customer customer = entity.getCustomer();
    String customerName = customer != null ? customer.getName() : null;
    Long customerId =
        customer != null && customer.getId() != null ? customer.getId().longValue() : null;

    Budget budget = entity.getBudget();
    BigDecimal budgetValue = budget != null ? budget.getValue() : null;

    return new MaintenanceRequestDetails(
        id,
        formatDateTime(entity.getCreatedAt()),
        customerName,
        customerId,
        entity.getDefect(),
        null,
        status,
        budgetValue,
        null);
  }

  public List<MaintenanceRequestDetails> toDetails(List<MaintenanceRequest> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toDetails).toList();
  }
}
