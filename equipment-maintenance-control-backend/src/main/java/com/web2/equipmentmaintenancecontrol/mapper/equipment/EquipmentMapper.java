package com.web2.equipmentmaintenancecontrol.mapper.equipment;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper extends BaseMapper {

  public String toDescription(Equipment equipment) {
    return equipment != null ? equipment.getDescription() : null;
  }

  public String toCategoryName(Equipment equipment) {
    EquipmentType type = equipment != null ? equipment.getType() : null;
    return type != null ? type.getDescription() : null;
  }
}
