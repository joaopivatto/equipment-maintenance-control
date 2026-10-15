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
import com.web2.equipmentmaintenancecontrol.repository.maintenance.BudgetRepository;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRepository;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import com.web2.equipmentmaintenancecontrol.service.equipment.EquipmentService;
import com.web2.equipmentmaintenancecontrol.service.profile.CustomerService;
import com.web2.equipmentmaintenancecontrol.service.profile.EmployeeService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceRequestService extends BaseService {

  private final MaintenanceRequestRepository repository;
  private final BudgetRepository budgetRepository;
  private final MaintenanceRepository maintenanceRepository;
  private final MaintenanceRequestMapper mapper;
  private final CustomerService customerService;
  private final EquipmentService equipmentService;
  private final EmployeeService employeeService;

  public MaintenanceRequestService(
      MaintenanceRequestRepository repository,
      BudgetRepository budgetRepository,
      MaintenanceRepository maintenanceRepository,
      MaintenanceRequestMapper mapper,
      CustomerService customerService,
      EquipmentService equipmentService,
      EmployeeService employeeService) {
    this.repository = repository;
    this.budgetRepository = budgetRepository;
    this.maintenanceRepository = maintenanceRepository;
    this.mapper = mapper;
    this.customerService = customerService;
    this.equipmentService = equipmentService;
    this.employeeService = employeeService;
  }

  @Transactional
  public MaintenanceRequestDetails create(CreateMaintenanceRequest request) {

    Customer customer = customerService.findById(request.customerId());
    Equipment equipment =
        equipmentService.createEntity(
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

  @Transactional(readOnly = true)
  public MaintenanceRequestDetails findById(Integer id) {
    return mapper.toDetails(findEntityById(id));
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findByCustomerId(Integer customerId) {
    customerService.findById(customerId);
    return mapper.toDetails(repository.findByCustomerIdOrderByCreatedAtAsc(customerId));
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findOpenRequests() {
    return mapper.toDetails(repository.findByStatus(MaintenanceRequestStatus.ABERTA));
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestDetails> findAll() {
    return mapper.toDetails(repository.findAll());
  }

  @Transactional
  public MaintenanceRequestDetails giveBudget(Integer id, CreateBudget request) {
    MaintenanceRequest entity = findEntityById(id);
    entity.requireNoBudget();
    Employee employee = findEmployeeById(request.employeeId());

    LocalDateTime budgetedAt = now();
    Budget budget = budgetRepository.save(new Budget(null, request.value(), employee, budgetedAt));

    entity.giveBudget(budget, budgetedAt);

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional
  public MaintenanceRequestDetails approve(Integer id, ApproveMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);

    entity.approve(now(), request.customerId());

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional
  public MaintenanceRequestDetails reject(Integer id, RejectMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);

    entity.reject(request.reason(), now(), request.customerId());

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional
  public MaintenanceRequestDetails performMaintenance(Integer id, CreateMaintenance request) {
    MaintenanceRequest entity = findEntityById(id);
    entity.requireNoMaintenance();
    Employee employee = findEmployeeById(request.employeeId());

    LocalDateTime performedAt = now();
    Maintenance maintenance =
        maintenanceRepository.save(
            new Maintenance(
                null,
                request.description(),
                request.customerInstructions(),
                employee,
                performedAt));

    entity.performMaintenance(maintenance, performedAt);

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional
  public MaintenanceRequestDetails redirect(Integer id, RedirectMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);
    Employee sourceEmployee = findEmployeeById(request.sourceEmployeeId());
    Employee destinationEmployee = findEmployeeById(request.destinationEmployeeId());

    LocalDateTime redirectedAt = now();
    entity.redirectTo(
        new Redirect(null, sourceEmployee, destinationEmployee, entity, redirectedAt),
        redirectedAt);

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional
  public MaintenanceRequestDetails pay(Integer id) {
    MaintenanceRequest entity = findEntityById(id);

    entity.pay(now());

    return mapper.toDetails(repository.save(entity));
  }

  @Transactional
  public MaintenanceRequestDetails finish(Integer id, FinishMaintenanceRequest request) {
    MaintenanceRequest entity = findEntityById(id);
    Employee employee = findEmployeeById(request.employeeId());

    entity.finish(employee, now());

    return mapper.toDetails(repository.save(entity));
  }

  private MaintenanceRequest findEntityById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new AppException(
                    ErrorCode.MAINTENANCE_REQUEST_NOT_FOUND,
                    "Solicitação de manutenção não encontrada com ID: " + id));
  }

  private Employee findEmployeeById(Integer id) {
    return employeeService.findEntityById(id);
  }
}
