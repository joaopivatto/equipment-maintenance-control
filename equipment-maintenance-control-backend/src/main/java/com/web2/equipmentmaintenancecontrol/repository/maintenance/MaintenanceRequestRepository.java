package com.web2.equipmentmaintenancecontrol.repository.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Integer> {}
