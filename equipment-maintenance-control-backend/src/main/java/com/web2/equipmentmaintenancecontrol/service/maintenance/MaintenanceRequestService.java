package com.web2.equipmentmaintenancecontrol.service.maintenance;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceRequestMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateBudget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceRequestService {

  private final MaintenanceRequestRepository repository;
  private final MaintenanceRequestMapper mapper;

  public MaintenanceRequestService(
      MaintenanceRequestRepository repository, MaintenanceRequestMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public MaintenanceRequestDetails findById(Integer id) {
    MaintenanceRequest entity =
            repository
                    .findById(id)
                    .orElseThrow(
                            () ->
                                    new AppException(
                                            ErrorCode.MAINTENANCE_REQUEST_NOT_FOUND, "Solicitação de manutenção não encontrada com ID: " + id));
    return mapper.toDetails(entity);
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findOpenRequests() {
    return mapper.toDetails(repository.findByStatus(MaintenanceRequestStatus.ABERTA));
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findAll() {
    return mapper.toDetails(repository.findAll());
  }
}
