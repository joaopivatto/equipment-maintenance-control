package com.web2.equipmentmaintenancecontrol.repository;

import com.web2.equipmentmaintenancecontrol.model.ActivatableEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ActiveRepository<T extends ActivatableEntity, ID> extends JpaRepository<T, ID> {

  @Override
  @Query("select e from #{#entityName} e where e.active = true")
  List<T> findAll();
}
