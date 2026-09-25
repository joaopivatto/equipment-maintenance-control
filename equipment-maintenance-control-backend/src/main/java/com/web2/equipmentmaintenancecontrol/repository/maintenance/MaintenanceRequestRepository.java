package com.web2.equipmentmaintenancecontrol.repository.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dtos.MaintenanceRequestResponseDTO;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Integer> {

  Collection<MaintenanceRequestResponseDTO> findByStatus(MaintenanceRequestStatus aberta);
}
