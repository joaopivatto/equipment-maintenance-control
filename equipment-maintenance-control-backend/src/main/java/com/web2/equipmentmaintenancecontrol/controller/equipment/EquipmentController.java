package com.web2.equipmentmaintenancecontrol.controller.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.CreateEquipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.UpdateEquipment;
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
  public ResponseEntity<Equipment> getEquipmentById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<Equipment>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{typeId}/type")
  public ResponseEntity<List<Equipment>> listByTypeId(@PathVariable Integer typeId) {
    return ResponseEntity.ok(service.findByTypeId(typeId));
  }

  @PostMapping
  public ResponseEntity<Equipment> create(@Valid @RequestBody CreateEquipment request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Equipment> update(
      @PathVariable Integer id, @Valid @RequestBody UpdateEquipment request) {
    return ResponseEntity.ok(service.update(id, request));
  }
}
