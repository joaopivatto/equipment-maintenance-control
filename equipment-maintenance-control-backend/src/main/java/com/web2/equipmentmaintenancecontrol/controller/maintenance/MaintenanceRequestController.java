package com.web2.equipmentmaintenancecontrol.controller.maintenance;

import com.web2.equipmentmaintenancecontrol.config.AccessGuard;
import com.web2.equipmentmaintenancecontrol.config.RequiresProfile;
import com.web2.equipmentmaintenancecontrol.config.TokenInterceptor;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.ApproveMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateBudget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.FinishMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RedirectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RejectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.service.maintenance.MaintenanceRequestService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maintenance-request")
public class MaintenanceRequestController {

  private final MaintenanceRequestService service;

  public MaintenanceRequestController(MaintenanceRequestService service) {
    this.service = service;
  }

  @PostMapping
  @RequiresProfile(ProfileType.CUSTOMER)
  public ResponseEntity<MaintenanceRequestDetails> create(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @Valid @RequestBody CreateMaintenanceRequest request) {
    AccessGuard.requireSameUser(userId, request.customerId());

    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MaintenanceRequestDetails> getById(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @RequestAttribute(TokenInterceptor.PROFILE_TYPE_ATTRIBUTE) ProfileType profileType,
      @PathVariable Integer id) {
    MaintenanceRequestDetails details = service.findById(id);

    if (profileType == ProfileType.CUSTOMER) {
      requireOwner(userId, details);
    }

    return ResponseEntity.ok(details);
  }

  @GetMapping
  @RequiresProfile(ProfileType.EMPLOYEE)
  public ResponseEntity<List<MaintenanceRequestDetails>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/open")
  @RequiresProfile(ProfileType.EMPLOYEE)
  public ResponseEntity<List<MaintenanceRequestDetails>> listOpen() {
    return ResponseEntity.ok(service.findOpenRequests());
  }

  @PostMapping("/{id}/budget")
  @RequiresProfile(ProfileType.EMPLOYEE)
  public ResponseEntity<MaintenanceRequestDetails> giveBudget(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @Valid @RequestBody CreateBudget request) {
    AccessGuard.requireSameUser(userId, request.employeeId());

    return ResponseEntity.ok(service.giveBudget(id, request));
  }

  @PostMapping("/{id}/approve")
  @RequiresProfile(ProfileType.CUSTOMER)
  public ResponseEntity<MaintenanceRequestDetails> approve(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @Valid @RequestBody ApproveMaintenanceRequest request) {
    AccessGuard.requireSameUser(userId, request.customerId());

    return ResponseEntity.ok(service.approve(id, request));
  }

  @PostMapping("/{id}/reject")
  @RequiresProfile(ProfileType.CUSTOMER)
  public ResponseEntity<MaintenanceRequestDetails> reject(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @Valid @RequestBody RejectMaintenanceRequest request) {
    AccessGuard.requireSameUser(userId, request.customerId());

    return ResponseEntity.ok(service.reject(id, request));
  }

  @PostMapping("/{id}/maintenance")
  @RequiresProfile(ProfileType.EMPLOYEE)
  public ResponseEntity<MaintenanceRequestDetails> performMaintenance(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @Valid @RequestBody CreateMaintenance request) {
    AccessGuard.requireSameUser(userId, request.employeeId());

    return ResponseEntity.ok(service.performMaintenance(id, request));
  }

  @PostMapping("/{id}/redirect")
  @RequiresProfile(ProfileType.EMPLOYEE)
  public ResponseEntity<MaintenanceRequestDetails> redirect(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @Valid @RequestBody RedirectMaintenanceRequest request) {
    AccessGuard.requireSameUser(userId, request.sourceEmployeeId());

    return ResponseEntity.ok(service.redirect(id, request));
  }

  @PostMapping("/{id}/pay")
  @RequiresProfile(ProfileType.CUSTOMER)
  public ResponseEntity<MaintenanceRequestDetails> pay(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id) {
    requireOwner(userId, service.findById(id));

    return ResponseEntity.ok(service.pay(id));
  }

  @PostMapping("/{id}/finish")
  @RequiresProfile(ProfileType.EMPLOYEE)
  public ResponseEntity<MaintenanceRequestDetails> finish(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @Valid @RequestBody FinishMaintenanceRequest request) {
    AccessGuard.requireSameUser(userId, request.employeeId());

    return ResponseEntity.ok(service.finish(id, request));
  }

  private void requireOwner(Integer userId, MaintenanceRequestDetails details) {
    if (!Objects.equals(details.customer().id(), userId)) {
      throw new AppException(ErrorCode.MAINTENANCE_REQUEST_NOT_OWNED);
    }
  }
}
