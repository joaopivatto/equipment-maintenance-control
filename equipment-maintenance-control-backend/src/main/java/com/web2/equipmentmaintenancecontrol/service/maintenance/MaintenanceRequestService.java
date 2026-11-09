package com.web2.equipmentmaintenancecontrol.service.maintenance;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.maintenance.MaintenanceRequestMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Budget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Maintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Redirect;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.ApproveMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateBudget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.CreateMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.FinishMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RedirectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.RejectMaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentService;
import com.web2.equipmentmaintenancecontrol.service.profile.CustomerService;
import com.web2.equipmentmaintenancecontrol.service.profile.EmployeeService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de aplicação das solicitações de manutenção. Cada operação de mudança de estado carrega a
 * entidade, delega a validação da transição para {@link MaintenanceRequest} e persiste o resultado.
 */
@Service
public class MaintenanceRequestService extends BaseService {

  private final MaintenanceRequestRepository repository;
  private final MaintenanceRequestMapper mapper;
  private final CustomerService customerService;
  private final EquipmentService equipmentService;
  private final EmployeeService employeeService;

  public MaintenanceRequestService(
      MaintenanceRequestRepository repository,
      MaintenanceRequestMapper mapper,
      CustomerService customerService,
      EquipmentService equipmentService,
      EmployeeService employeeService) {
    this.repository = repository;
    this.mapper = mapper;
    this.customerService = customerService;
    this.equipmentService = equipmentService;
    this.employeeService = employeeService;
  }

  /** Abre uma nova solicitação para o cliente, cadastrando o equipamento informado. */
  @Transactional
  public MaintenanceRequestDetails create(CreateMaintenanceRequest request) {

    Customer customer = customerService.findById(request.customerId());
    Equipment equipment =
        equipmentService.newEquipment(
            request.equipment().description(), request.equipment().typeId());

    MaintenanceRequest entity =
        MaintenanceRequest.builder()
            .customer(customer)
            .equipment(equipment)
            .defect(request.defectDescription())
            .createdAt(now())
            .build();

    return mapper.toDetails(repository.save(entity));
  }

  /** Retorna os detalhes de uma solicitação pelo id. */
  @Transactional(readOnly = true)
  public MaintenanceRequestDetails findById(Integer id) {
    return mapper.toDetails(findEntityById(id));
  }

  /** Lista as solicitações do cliente em ordem de criação, validando se o cliente existe. */
  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findByCustomerId(Integer customerId) {
    customerService.findById(customerId);
    return mapper.toDetails(repository.findByCustomerIdOrderByCreatedAtAsc(customerId));
  }

  /** Lista apenas as solicitações com status ABERTA, aguardando orçamento. */
  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findOpenRequests() {
    return mapper.toDetails(repository.findByStatus(MaintenanceRequestStatus.ABERTA));
  }

  /** Lista todas as solicitações, independentemente do status. */
  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findAll() {
    return mapper.toDetails(repository.findAll());
  }

  /** Registra o orçamento feito pelo funcionário (ABERTA -> ORCADA). */
  @Transactional
  public MaintenanceRequestDetails giveBudget(Integer id, CreateBudget request) {
    MaintenanceRequest entity = findEntityById(id);
    entity.requireNoBudget();
    Employee employee = findEmployeeById(request.employeeId());

    LocalDateTime budgetedAt = now();
    entity.giveBudget(new Budget(request.value(), employee, budgetedAt), budgetedAt);

    return mapper.toDetails(repository.save(entity));
  }

  /** Cliente aprova o orçamento (ORCADA ou REJEITADA -> APROVADA). */
  @Transactional
  public MaintenanceRequestDetails approve(Integer id, ApproveMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);

    entity.approve(now(), request.customerId());

    return mapper.toDetails(repository.save(entity));
  }

  /** Cliente rejeita o orçamento informando o motivo (ORCADA -> REJEITADA). */
  @Transactional
  public MaintenanceRequestDetails reject(Integer id, RejectMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);

    entity.reject(request.reason(), now(), request.customerId());

    return mapper.toDetails(repository.save(entity));
  }

  /** Registra a manutenção realizada e as orientações ao cliente (-> ARRUMADA). */
  @Transactional
  public MaintenanceRequestDetails performMaintenance(Integer id, CreateMaintenance request) {
    MaintenanceRequest entity = findEntityById(id);
    entity.requireNoMaintenance();
    Employee employee = findEmployeeById(request.employeeId());

    LocalDateTime performedAt = now();
    Maintenance maintenance =
        new Maintenance(
            request.description(), request.customerInstructions(), employee, performedAt);

    entity.performMaintenance(maintenance, performedAt);

    return mapper.toDetails(repository.save(entity));
  }

  /** Redireciona a solicitação de um funcionário para outro (-> REDIRECIONADA). */
  @Transactional
  public MaintenanceRequestDetails redirect(Integer id, RedirectMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);
    Employee sourceEmployee = findEmployeeById(request.sourceEmployeeId());
    Employee destinationEmployee = findEmployeeById(request.destinationEmployeeId());

    LocalDateTime redirectedAt = now();
    entity.redirectTo(new Redirect(sourceEmployee, destinationEmployee), redirectedAt);

    return mapper.toDetails(repository.save(entity));
  }

  /** Registra o pagamento do serviço pelo cliente (ARRUMADA -> PAGA). */
  @Transactional
  public MaintenanceRequestDetails pay(Integer id) {
    MaintenanceRequest entity = findEntityById(id);

    entity.pay(now());

    return mapper.toDetails(repository.save(entity));
  }

  /** Funcionário finaliza a solicitação após o pagamento (PAGA -> FINALIZADA). */
  @Transactional
  public MaintenanceRequestDetails finish(Integer id, FinishMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);
    Employee employee = findEmployeeById(request.employeeId());

    entity.finish(employee, now());

    return mapper.toDetails(repository.save(entity));
  }

  /** Busca a entidade ou lança MAINTENANCE_REQUEST_NOT_FOUND. */
  private MaintenanceRequest findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new AppException(
                    ErrorCode.MAINTENANCE_REQUEST_NOT_FOUND,
                    "Solicitação de manutenção não encontrada com ID: " + id));
  }

  /** Busca o funcionário pelo id, delegando ao EmployeeService. */
  private Employee findEmployeeById(Integer id) {
    return employeeService.findEntityById(id);
  }
}
