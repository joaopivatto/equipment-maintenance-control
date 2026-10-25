package com.web2.equipmentmaintenancecontrol.controller.profile;

import com.web2.equipmentmaintenancecontrol.config.AccessGuard;
import com.web2.equipmentmaintenancecontrol.config.TokenInterceptor;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.service.maintenance.MaintenanceRequestService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
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
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @RequestAttribute(TokenInterceptor.PROFILE_TYPE_ATTRIBUTE) ProfileType profileType,
      @PathVariable Integer id) {
    if (profileType == ProfileType.CUSTOMER) {
      AccessGuard.requireSameUser(userId, id);
    }

    return ResponseEntity.ok(maintenanceRequestService.findByCustomerId(id));
  }
}
