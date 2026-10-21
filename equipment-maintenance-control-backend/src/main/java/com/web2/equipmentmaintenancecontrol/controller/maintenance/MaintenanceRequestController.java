package com.web2.equipmentmaintenancecontrol.controller.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.ApproveMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateBudget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.FinishMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RedirectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RejectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.service.maintenance.MaintenanceRequestService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<MaintenanceRequestDetails> create(
      @Valid @RequestBody CreateMaintenanceRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MaintenanceRequestDetails> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<MaintenanceRequestDetails>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/open")
  public ResponseEntity<List<MaintenanceRequestDetails>> listOpen() {
    return ResponseEntity.ok(service.findOpenRequests());
  }

  @PostMapping("/{id}/budget")
  public ResponseEntity<MaintenanceRequestDetails> giveBudget(
      @PathVariable Integer id, @Valid @RequestBody CreateBudget request) {
    return ResponseEntity.ok(service.giveBudget(id, request));
  }

  @PostMapping("/{id}/approve")
  public ResponseEntity<MaintenanceRequestDetails> approve(
      @PathVariable Integer id, @Valid @RequestBody ApproveMaintenanceRequest request) {
    return ResponseEntity.ok(service.approve(id, request));
  }

  @PostMapping("/{id}/reject")
  public ResponseEntity<MaintenanceRequestDetails> reject(
      @PathVariable Integer id, @Valid @RequestBody RejectMaintenanceRequest request) {
    return ResponseEntity.ok(service.reject(id, request));
  }

  @PostMapping("/{id}/maintenance")
  public ResponseEntity<MaintenanceRequestDetails> performMaintenance(
      @PathVariable Integer id, @Valid @RequestBody CreateMaintenance request) {
    return ResponseEntity.ok(service.performMaintenance(id, request));
  }

  @PostMapping("/{id}/redirect")
  public ResponseEntity<MaintenanceRequestDetails> redirect(
      @PathVariable Integer id, @Valid @RequestBody RedirectMaintenanceRequest request) {
    return ResponseEntity.ok(service.redirect(id, request));
  }

  @PostMapping("/{id}/pay")
  public ResponseEntity<MaintenanceRequestDetails> pay(@PathVariable Integer id) {
    return ResponseEntity.ok(service.pay(id));
  }

  @PostMapping("/{id}/finish")
  public ResponseEntity<MaintenanceRequestDetails> finish(
      @PathVariable Integer id, @Valid @RequestBody FinishMaintenanceRequest request) {
    return ResponseEntity.ok(service.finish(id, request));
  }
}
