package com.web2.equipmentmaintenancecontrol.repository.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {}
