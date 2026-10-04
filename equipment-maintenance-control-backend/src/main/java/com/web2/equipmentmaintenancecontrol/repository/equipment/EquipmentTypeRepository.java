package com.web2.equipmentmaintenancecontrol.repository.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Integer> {

    List<EquipmentType> findByActiveTrue();
}