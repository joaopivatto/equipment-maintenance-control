package com.web2.equipmentmaintenancecontrol.service.maintenance;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceRequestMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Budget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestHistory;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateBudget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RejectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.PerformMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Maintenance;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import com.web2.equipmentmaintenancecontrol.repository.profile.EmployeeRepository;
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
  private final EmployeeRepository employeeRepository;

  public MaintenanceRequestService(
      MaintenanceRequestRepository repository,
      MaintenanceRequestMapper mapper,
      CustomerService customerService,
      EquipmentService equipmentService,
      EmployeeRepository employeeRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.customerService = customerService;
    this.equipmentService = equipmentService;
    this.employeeRepository = employeeRepository;
  }

  @Transactional
  public MaintenanceRequestDetails create(CreateMaintenanceRequest request) {

    Customer customer = customerService.findById(request.customerId());
    Equipment equipment = equipmentService.findById(request.equipmentId());

    MaintenanceRequest entity = MaintenanceRequest.builder()
        .customer(customer)
        .equipment(equipment)
        .defect(request.defectDescription())
        .createdAt(now())
        .build();

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional(readOnly = true)
  public MaintenanceRequestDetails findById(Integer id) {
    return mapper.toDetails(findEntityById(id));
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findOpenRequests() {
    return mapper.toDetails(repository.findByStatus(MaintenanceRequestStatus.ABERTA));
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findAll() {
    return mapper.toDetails(repository.findAll());
  }

  /** RF012 - Efetuar Orçamento. Só é permitido a partir do estado ABERTA. */
  @Transactional
  public MaintenanceRequestDetails giveBudget(Integer id, CreateBudget request) {
    MaintenanceRequest entity = findEntityById(id);
    requireStatus(entity, MaintenanceRequestStatus.ABERTA);

    Employee employee = employeeRepository
        .findById(request.employeeId())
        .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

    Budget budget = new Budget(null, request.value(), employee, now());
    entity.setBudget(budget);
    entity.addHistory(
        new MaintenanceRequestHistory(MaintenanceRequestStatus.ORCADA, now(), employee));

    return mapper.toDetails(repository.save(entity));
  }

  /** RF006 - Aprovar Serviço. Só é permitido a partir do estado ORCADA. */
  @Transactional
  public MaintenanceRequestDetails approve(Integer id) {
    MaintenanceRequest entity = findEntityById(id);
    requireStatus(entity, MaintenanceRequestStatus.ORCADA);

    entity.addHistory(new MaintenanceRequestHistory(MaintenanceRequestStatus.APROVADA, now()));

    return mapper.toDetails(repository.save(entity));
  }

  /** RF007 - Rejeitar Serviço. Só é permitido a partir do estado ORCADA. */
  @Transactional
  public MaintenanceRequestDetails reject(Integer id, RejectMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);
    requireStatus(entity, MaintenanceRequestStatus.ORCADA);

    entity.addHistory(
        new MaintenanceRequestHistory(
            MaintenanceRequestStatus.REJEITADA, now(), null, request.reason()));

    return mapper.toDetails(repository.save(entity));
  }

  /** RF009 - Resgatar Serviço. Só é permitido a partir do estado REJEITADA. */
  @Transactional
  public MaintenanceRequestDetails rescue(Integer id) {
    MaintenanceRequest entity = findEntityById(id);
    requireStatus(entity, MaintenanceRequestStatus.REJEITADA);

    entity.addHistory(new MaintenanceRequestHistory(MaintenanceRequestStatus.APROVADA, now()));

    return mapper.toDetails(repository.save(entity));
  }

  /** RF010 - Pagar Serviço. Só é permitido a partir do estado ARRUMADA. */
  @Transactional
  public MaintenanceRequestDetails pay(Integer id) {
    MaintenanceRequest entity = findEntityById(id);
    requireStatus(entity, MaintenanceRequestStatus.ARRUMADA);

    entity.setPaymentDate(today());
    entity.addHistory(new MaintenanceRequestHistory(MaintenanceRequestStatus.PAGA, now()));

    return mapper.toDetails(repository.save(entity));
  }

  /**
   * RF014 - Efetuar Manutenção. Só é permitido a partir de APROVADA ou
   * REDIRECIONADA.
   */
  @Transactional
  public MaintenanceRequestDetails performMaintenance(
      Integer id,
      PerformMaintenanceRequest request) {

    // 1. Busca a solicitação
    MaintenanceRequest entity = findEntityById(id);

    // 2. Valida se o status atual permite a manutenção
    if (entity.getStatus() != MaintenanceRequestStatus.APROVADA
        && entity.getStatus() != MaintenanceRequestStatus.REDIRECIONADA) {
      throw new IllegalArgumentException(
          "A manutenção só pode ser efetuada para solicitações APROVADAS ou REDIRECIONADAS");
    }

    // 3. Busca o funcionário que executou o serviço
    Employee employee = employeeRepository.findById(request.employeeId())
        .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

    // 4. Cria o registro da manutenção executada
    Maintenance maintenance = new Maintenance();
    maintenance.setDescription(request.description());
    maintenance.setCustomerInstructions(request.customerInstructions());
    maintenance.setEmployee(employee);
    maintenance.setCreatedAt(today());

    // 5. Associa à solicitação e atualiza para ARRUMADA
    entity.setMaintenance(maintenance);
    entity.addHistory(new MaintenanceRequestHistory(MaintenanceRequestStatus.ARRUMADA, now()));

    // 6. Salva e retorna o DTO de detalhes return
    return mapper.toDetails(repository.save(entity));
  }

  private MaintenanceRequest findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new AppException(
                ErrorCode.MAINTENANCE_REQUEST_NOT_FOUND,
                "Solicitação de manutenção não encontrada com ID: " + id));
  }

  private void requireStatus(MaintenanceRequest entity, MaintenanceRequestStatus expected) {
    if (entity.getStatus() != expected) {
      throw new AppException(
          ErrorCode.INVALID_MAINTENANCE_REQUEST_STATUS,
          "Ação não permitida para o estado atual: " + entity.getStatus());
    }
  }
}
