package com.web2.equipmentmaintenancecontrol.controller.profile;

import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.service.maintenance.MaintenanceRequestService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final MaintenanceRequestService maintenanceRequestService;

  public CustomerController(MaintenanceRequestService maintenanceRequestService) {
    this.maintenanceRequestService = maintenanceRequestService;
  }

  @GetMapping("/{id}/maintenance-requests")
  public ResponseEntity<List<MaintenanceRequestDetails>> listMaintenanceRequests(
      @PathVariable Integer id) {
    return ResponseEntity.ok(maintenanceRequestService.findByCustomerId(id));
  }
}
