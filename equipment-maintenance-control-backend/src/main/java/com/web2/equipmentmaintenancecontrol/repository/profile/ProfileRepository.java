package com.web2.equipmentmaintenancecontrol.repository.profile;

import com.web2.equipmentmaintenancecontrol.model.profile.Profile;
import com.web2.equipmentmaintenancecontrol.repository.ActiveRepository;

public interface ProfileRepository extends ActiveRepository<Profile, Integer> {

  Profile findByEmail(String email);
}
