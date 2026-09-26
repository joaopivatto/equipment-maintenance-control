package com.web2.equipmentmaintenancecontrol.service.profile;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.repository.profile.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  public Customer findById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new AppException(
                    ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado com ID: " + id));
  }
}
