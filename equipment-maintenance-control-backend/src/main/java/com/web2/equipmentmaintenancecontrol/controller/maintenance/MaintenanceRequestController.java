package com.web2.equipmentmaintenancecontrol.controller.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
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
}
