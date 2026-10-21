package com.web2.equipmentmaintenancecontrol.service.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService extends BaseService {

  private final EquipmentTypeService equipmentTypeService;

  public EquipmentService(EquipmentTypeService equipmentTypeService) {
    this.equipmentTypeService = equipmentTypeService;
  }

  public Equipment newEquipment(String description, Integer typeId) {
    return new Equipment(description, equipmentTypeService.findEntityById(typeId));
  }
}
