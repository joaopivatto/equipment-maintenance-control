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

  /** RF011 - Lista apenas as solicitações com status ABERTA */
  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponseDTO> findOpenRequests() {
    return repository.findAll().stream()
        .map(this::toDTO)
        .filter(request -> request.status() == MaintenanceRequestStatus.ABERTA)
        .collect(Collectors.toList());
  }

  /** Lista todas as solicitações sem filtro de status */
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

  /** RF012 - Efetuar orçamento (será delegado para o BudgetService) */
  @Transactional
  public MaintenanceRequestResponseDTO createBudget(Integer id, CreateBudgetDTO dto) {
    // TODO: A criação do orçamento será implementada no BudgetService dedicado
    throw new UnsupportedOperationException(
        "Funcionalidade de orçamento será implementada no BudgetService");
  }

  // Mapeia a Entidade para o Record Java
  private MaintenanceRequestResponseDTO toDTO(MaintenanceRequest entity) {
    Long idAsLong = entity.getId() != null ? entity.getId().longValue() : null;

    // Obtém o status do último registro de histórico, se existir; caso contrário,
    // ABERTA
    MaintenanceRequestStatus status = MaintenanceRequestStatus.ABERTA;
    if (entity.getHistory() != null && !entity.getHistory().isEmpty()) {
      status = entity.getHistory().get(0).getStatus();
    }

    return new MaintenanceRequestResponseDTO(
        idAsLong,
        entity.getCreatedAt(),
        null, // customerName
        null, // customerId
        entity.getDefect(),
        null, // categoryName
        status,
        null, // budgetValue
        null // justification
        );
  }
}
