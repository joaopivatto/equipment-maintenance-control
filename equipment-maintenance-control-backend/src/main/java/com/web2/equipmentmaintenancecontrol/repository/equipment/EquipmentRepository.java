package com.web2.equipmentmaintenancecontrol.repository.equipment;

import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {}
