package com.web2.equipmentmaintenancecontrol.service.equipment;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentTypeMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeRequest;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeResponse;
import com.web2.equipmentmaintenancecontrol.repository.equipment.EquipmentTypeRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentTypeService extends BaseService {

  private final EquipmentTypeRepository repository;
  private final EquipmentTypeMapper mapper;

  public EquipmentTypeService(EquipmentTypeRepository repository, EquipmentTypeMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public EquipmentTypeResponse findById(Integer id) {
    return mapper.toResponse(findEntityById(id));
  }

  public List<EquipmentTypeResponse> findAll() {
    return mapper.toResponse(repository.findAll());
  }

  @Transactional
  public EquipmentTypeResponse create(EquipmentTypeRequest request) {
    return mapper.toResponse(repository.save(new EquipmentType(null, request.description())));
  }

  @Transactional
  public EquipmentTypeResponse update(Integer id, EquipmentTypeRequest request) {
    EquipmentType equipmentType = findEntityById(id);
    equipmentType.setDescription(request.description());
    return mapper.toResponse(repository.save(equipmentType));
  }

  @Transactional
  public void delete(Integer id) {
    EquipmentType equipmentType = findEntityById(id);

    if (!equipmentType.getActive()) {
      throw new AppException(ErrorCode.EQUIPMENT_TYPE_ALREADY_INACTIVE);
    }

    repository.delete(equipmentType);
  }

  public EquipmentType findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND));
  }
}
