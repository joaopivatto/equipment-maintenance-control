package com.web2.equipmentmaintenancecontrol.service.maintenance;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceRequestMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentService;
import com.web2.equipmentmaintenancecontrol.service.profile.CustomerService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceRequestService extends BaseService {

  private final MaintenanceRequestRepository repository;
  private final MaintenanceRequestMapper mapper;
  private final CustomerService customerService;
  private final EquipmentService equipmentService;

  public MaintenanceRequestService(
      MaintenanceRequestRepository repository,
      MaintenanceRequestMapper mapper,
      CustomerService customerService,
      EquipmentService equipmentService) {
    this.repository = repository;
    this.mapper = mapper;
    this.customerService = customerService;
    this.equipmentService = equipmentService;
  }

  @Transactional
  public MaintenanceRequestDetails create(CreateMaintenanceRequest request) {

    Customer customer = customerService.findById(request.customerId());
    Equipment equipment = equipmentService.findById(request.equipmentId());

    MaintenanceRequest entity =
        MaintenanceRequest.builder()
            .customer(customer)
            .equipment(equipment)
            .defect(request.defectDescription())
            .createdAt(now())
            .build();

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional(readOnly = true)
  public MaintenanceRequestDetails findById(Integer id) {
    MaintenanceRequest entity =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(
                        ErrorCode.MAINTENANCE_REQUEST_NOT_FOUND,
                        "Solicitação de manutenção não encontrada com ID: " + id));
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
