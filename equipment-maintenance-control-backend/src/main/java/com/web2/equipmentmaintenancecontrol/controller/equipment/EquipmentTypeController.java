package com.web2.equipmentmaintenancecontrol.controller.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeRequest;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentTypeService;
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
@RequestMapping("/equipment-type")
public class EquipmentTypeController {

  private final EquipmentTypeService service;

  public EquipmentTypeController(EquipmentTypeService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EquipmentType> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<EquipmentType>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @PostMapping
  public ResponseEntity<EquipmentType> create(@Valid @RequestBody EquipmentTypeRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EquipmentType> update(
      @PathVariable Integer id, @Valid @RequestBody EquipmentTypeRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }
}
