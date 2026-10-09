package com.web2.equipmentmaintenancecontrol.mapper.profile;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.CustomerResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends BaseMapper {

  public CustomerResponse toResponse(Customer entity) {
    if (entity == null) {
      return null;
    }
    return new CustomerResponse(entity.getId(), entity.getName(), entity.getEmail());
  }

  public List<CustomerResponse> toResponse(List<Customer> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toResponse).toList();
  }
}
