package com.web2.equipmentmaintenancecontrol.mapper.equipment;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EquipmentTypeMapper extends BaseMapper {

  public EquipmentTypeResponse toResponse(EquipmentType entity) {
    if (entity == null) {
      return null;
    }
    return new EquipmentTypeResponse(entity.getId(), entity.getDescription());
  }

  public List<EquipmentTypeResponse> toResponse(List<EquipmentType> entities) {
    if (entities == null) {
      return List.of();
    }
    return entities.stream().map(this::toResponse).toList();
  }
}
