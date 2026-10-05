package com.web2.equipmentmaintenancecontrol.controller.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentRequest;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentResponse;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

  private final EquipmentService service;

  public EquipmentController(EquipmentService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<EquipmentResponse>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{typeId}/type")
  public ResponseEntity<List<EquipmentResponse>> listByTypeId(@PathVariable Integer typeId) {
    return ResponseEntity.ok(service.findByTypeId(typeId));
  }

  @PostMapping
  public ResponseEntity<EquipmentResponse> create(@Valid @RequestBody EquipmentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EquipmentResponse> update(
      @PathVariable Integer id, @Valid @RequestBody EquipmentRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }
}
