package com.web2.equipmentmaintenancecontrol.repository.profile;

import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.repository.ActiveRepository;

public interface CustomerRepository extends ActiveRepository<Customer, Integer> {
  Customer findByCpf(String cpf);

  Customer findByEmail(String email);
}
