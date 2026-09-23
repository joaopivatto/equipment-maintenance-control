package com.web2.equipmentmaintenancecontrol.service.equipment;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dtos.EquipmentRequest;
import com.web2.equipmentmaintenancecontrol.repository.equipment.EquipmentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService {

  private final EquipmentRepository repository;
  private final EquipmentTypeService equipmentTypeService;

  public EquipmentService(
      EquipmentRepository repository, EquipmentTypeService equipmentTypeService) {
    this.repository = repository;
    this.equipmentTypeService = equipmentTypeService;
  }

  public Equipment findById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EQUIPMENT_NOT_FOUND));
  }

  public List<Equipment> findAll() {
    return repository.findAll();
  }

  public List<Equipment> findByTypeId(Integer typeId) {
    equipmentTypeService.findById(typeId);
    return repository.findByTypeId(typeId);
  }

  public Equipment create(EquipmentRequest request) {
    EquipmentType type = equipmentTypeService.findById(request.typeId());
    return repository.save(new Equipment(null, request.description(), type));
  }

  public Equipment update(Integer id, EquipmentRequest request) {
    Equipment equipment = findById(id);
    equipment.setDescription(request.description());
    equipment.setType(equipmentTypeService.findById(request.typeId()));
    return repository.save(equipment);
  }
}
