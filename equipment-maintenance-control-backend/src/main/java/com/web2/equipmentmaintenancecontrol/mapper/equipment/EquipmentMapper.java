package com.web2.equipmentmaintenancecontrol.mapper.equipment;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper extends BaseMapper {

  private final EquipmentTypeMapper equipmentTypeMapper;

  public EquipmentMapper(EquipmentTypeMapper equipmentTypeMapper) {
    this.equipmentTypeMapper = equipmentTypeMapper;
  }

  public EquipmentResponse toResponse(Equipment entity) {
    if (entity == null) {
      return null;
    }
    return new EquipmentResponse(
        entity.getId(), entity.getDescription(), equipmentTypeMapper.toResponse(entity.getType()));
  }

  public List<EquipmentResponse> toResponse(List<Equipment> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toResponse).toList();
  }

  public Integer toId(Equipment equipment) {
    return equipment != null ? equipment.getId() : null;
  }

  public Integer toCategoryId(Equipment equipment) {
    EquipmentType type = equipment != null ? equipment.getType() : null;
    return type != null ? type.getId() : null;
  }

  public String toDescription(Equipment equipment) {
    return equipment != null ? equipment.getDescription() : null;
  }

  public String toCategoryName(Equipment equipment) {
    EquipmentType type = equipment != null ? equipment.getType() : null;
    return type != null ? type.getDescription() : null;
  }
}
