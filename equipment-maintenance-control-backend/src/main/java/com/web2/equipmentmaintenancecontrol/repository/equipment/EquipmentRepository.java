package com.web2.equipmentmaintenancecontrol.repository.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.repository.ActiveRepository;
import java.util.List;

public interface EquipmentRepository extends ActiveRepository<Equipment, Integer> {

  List<Equipment> findByTypeId(Integer typeId);
}
