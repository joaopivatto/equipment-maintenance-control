package com.web2.equipmentmaintenancecontrol.repository.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestHistoryRepository
    extends JpaRepository<MaintenanceRequestHistory, Integer> {}
