package com.web2.equipmentmaintenancecontrol.service.equipment;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeRequest;
import com.web2.equipmentmaintenancecontrol.repository.equipment.EquipmentTypeRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EquipmentTypeService extends BaseService {

  private final EquipmentTypeRepository repository;

  public EquipmentTypeService(EquipmentTypeRepository repository) {
    this.repository = repository;
  }

  public EquipmentType findById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND));
  }

  public List<EquipmentType> findAll() {
    return repository.findAll();
  }

  public EquipmentType create(EquipmentTypeRequest request) {
    return repository.save(new EquipmentType(null, request.description()));
  }

  public EquipmentType update(Integer id, EquipmentTypeRequest request) {
    EquipmentType equipmentType = findById(id);
    equipmentType.setDescription(request.description());
    return repository.save(equipmentType);
  }
}
