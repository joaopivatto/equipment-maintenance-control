package com.web2.equipmentmaintenancecontrol.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class BaseService {

  protected static final ZoneId APPLICATION_ZONE = ZoneId.of("America/Sao_Paulo");

  protected LocalDateTime now() {
    return LocalDateTime.now(APPLICATION_ZONE);
  }

  protected LocalDate today() {
    return LocalDate.now(APPLICATION_ZONE);
  }
}
