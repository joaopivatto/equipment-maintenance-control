package com.web2.equipmentmaintenancecontrol.repository.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

  List<Equipment> findByTypeId(Integer typeId);
}
