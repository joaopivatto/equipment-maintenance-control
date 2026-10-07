package com.web2.equipmentmaintenancecontrol.controller.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeRequest;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeResponse;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentTypeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment-type")
public class EquipmentTypeController {

  private final EquipmentTypeService service;

  public EquipmentTypeController(EquipmentTypeService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EquipmentTypeResponse> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<EquipmentTypeResponse>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @PostMapping
  public ResponseEntity<EquipmentTypeResponse> create(
      @Valid @RequestBody EquipmentTypeRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EquipmentTypeResponse> update(
      @PathVariable Integer id, @Valid @RequestBody EquipmentTypeRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
          @PathVariable Integer id) {

    service.delete(id);

    return ResponseEntity.noContent().build();
  }
}
