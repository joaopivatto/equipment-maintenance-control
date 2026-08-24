package com.web2.equipmentmaintenancecontrol.repository;

import com.web2.equipmentmaintenancecontrol.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {}
