package com.web2.equipmentmaintenancecontrol.service.equipment;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentRequest;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentResponse;
import com.web2.equipmentmaintenancecontrol.repository.equipment.EquipmentRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService extends BaseService {

  private final EquipmentRepository repository;
  private final EquipmentTypeService equipmentTypeService;
  private final EquipmentMapper mapper;

  public EquipmentService(
      EquipmentRepository repository,
      EquipmentTypeService equipmentTypeService,
      EquipmentMapper mapper) {
    this.repository = repository;
    this.equipmentTypeService = equipmentTypeService;
    this.mapper = mapper;
  }

  public EquipmentResponse findById(Integer id) {
    return mapper.toResponse(findEntityById(id));
  }

  public List<EquipmentResponse> findAll() {
    return mapper.toResponse(repository.findAll());
  }

  public List<EquipmentResponse> findByTypeId(Integer typeId) {
    equipmentTypeService.findEntityById(typeId);
    return mapper.toResponse(repository.findByTypeId(typeId));
  }

  public EquipmentResponse create(EquipmentRequest request) {
    EquipmentType type = equipmentTypeService.findEntityById(request.type());
    return mapper.toResponse(repository.save(new Equipment(null, request.description(), type)));
  }

  public EquipmentResponse update(Integer id, EquipmentRequest request) {
    Equipment equipment = findEntityById(id);
    equipment.setDescription(request.description());
    equipment.setType(equipmentTypeService.findEntityById(request.type()));
    return mapper.toResponse(repository.save(equipment));
  }

  public Equipment findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EQUIPMENT_NOT_FOUND));
  }
}
