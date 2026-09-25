package com.web2.equipmentmaintenancecontrol.service.maintenance;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dtos.CreateBudgetDTO;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dtos.MaintenanceRequestResponseDTO;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceRequestService {

  private final MaintenanceRequestRepository repository;

  public MaintenanceRequestService(MaintenanceRequestRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponseDTO> findOpenRequests() {
    return repository.findByStatus(MaintenanceRequestStatus.ABERTA).stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponseDTO> findAll() {
    return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public MaintenanceRequestResponseDTO findById(Integer id) {
    MaintenanceRequest entity =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(
                        ErrorCode.EQUIPMENT_NOT_FOUND, "Solicitação não encontrada com ID: " + id));
    return toDTO(entity);
  }

  @Transactional
  public MaintenanceRequestResponseDTO createBudget(Integer id, CreateBudgetDTO dto) {
    // TODO: A criação do orçamento será implementada no BudgetService dedicado
    throw new UnsupportedOperationException(
        "Funcionalidade de orçamento será implementada no BudgetService");
  }

  private MaintenanceRequestResponseDTO toDTO(MaintenanceRequest entity) {
    Long idAsLong = entity.getId() != null ? entity.getId().longValue() : null;

    MaintenanceRequestStatus status =
        entity.getStatus() != null ? entity.getStatus() : MaintenanceRequestStatus.ABERTA;

    return new MaintenanceRequestResponseDTO(
        idAsLong, entity.getCreatedAt(), null, null, entity.getDefect(), null, status, null, null);
  }
}
