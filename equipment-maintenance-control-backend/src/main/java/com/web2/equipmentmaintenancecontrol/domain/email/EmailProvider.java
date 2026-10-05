package com.web2.equipmentmaintenancecontrol.domain.email;

public interface EmailProvider {
  public void dispatch(Email email);
}
