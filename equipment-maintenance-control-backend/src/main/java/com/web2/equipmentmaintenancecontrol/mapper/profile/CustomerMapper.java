package com.web2.equipmentmaintenancecontrol.mapper.profile;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.profile.Address;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.AddressResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.CustomerResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends BaseMapper {

  public CustomerResponse toResponse(Customer entity) {
    if (entity == null) {
      return null;
    }
    String phone = entity.getPhone() != null ? entity.getPhone().getNumber() : null;
    return new CustomerResponse(
        entity.getId(),
        entity.getName(),
        entity.getEmail(),
        entity.getCpf(),
        phone,
        toAddressResponse(entity.getAddress()));
  }

  public List<CustomerResponse> toResponse(List<Customer> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toResponse).toList();
  }

  private AddressResponse toAddressResponse(Address address) {
    if (address == null) {
      return null;
    }
    return new AddressResponse(
        address.getZipCode(),
        address.getStreet(),
        address.getNumber(),
        address.getComplement(),
        address.getNeighborhood(),
        address.getCity(),
        address.getState());
  }
}
