package com.web2.equipmentmaintenancecontrol.repository;

import com.web2.equipmentmaintenancecontrol.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

  Profile findByEmail(String email);
}
