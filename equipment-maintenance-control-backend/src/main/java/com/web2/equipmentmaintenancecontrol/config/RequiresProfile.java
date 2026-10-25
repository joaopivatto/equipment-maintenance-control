package com.web2.equipmentmaintenancecontrol.config;

import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresProfile {

  ProfileType value();
}
