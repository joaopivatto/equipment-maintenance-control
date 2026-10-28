package com.web2.equipmentmaintenancecontrol.support;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Budget;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Maintenance;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.Address;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.model.profile.Phone;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public final class Fixtures {

  public static final LocalDateTime AT = LocalDateTime.of(2026, 3, 10, 9, 30);
  public static final HashedSaltedPassword PASSWORD = new HashedSaltedPassword("hash", "salt");

  private Fixtures() {}

  public static Employee employee(Integer id) {
    return employee(id, "Funcionário " + id);
  }

  public static Employee employee(Integer id, String name) {
    Employee employee =
        new Employee(name, "func" + id + "@web2.com", LocalDate.of(1995, 6, 15), PASSWORD);
    return withId(employee, id);
  }

  public static Customer customer(Integer id) {
    return customer(id, "Cliente " + id);
  }

  public static Customer customer(Integer id, String name) {
    Customer customer =
        new Customer(
            name,
            "cliente" + id + "@web2.com",
            PASSWORD,
            "1234567890" + id,
            address(),
            new Phone("41999990000"));
    return withId(customer, id);
  }

  public static Address address() {
    return new Address("80010000", "Rua das Flores", 100, "Apto 10", "Centro", "Curitiba", "PR");
  }

  public static EquipmentType equipmentType(Integer id) {
    return equipmentType(id, "Notebook");
  }

  public static EquipmentType equipmentType(Integer id, String description) {
    return withId(new EquipmentType(description), id);
  }

  public static Equipment equipment(Integer id) {
    return withId(new Equipment("Notebook Dell Inspiron", equipmentType(7)), id);
  }

  public static Budget budget(Integer id, Employee employee) {
    return withId(new Budget(new BigDecimal("450.00"), employee, AT), id);
  }

  public static Maintenance maintenance(Integer id, Employee employee) {
    return withId(
        new Maintenance("Troca da fonte", "Evitar uso sem estabilizador", employee, AT), id);
  }

  public static MaintenanceRequest openRequest(Integer id, Customer customer) {
    MaintenanceRequest request =
        MaintenanceRequest.builder()
            .customer(customer)
            .equipment(equipment(50))
            .defect("Não liga")
            .createdAt(AT)
            .build();
    return withId(request, id);
  }

  public static MaintenanceRequest budgetedRequest(
      Integer id, Customer customer, Employee employee) {
    MaintenanceRequest request = openRequest(id, customer);
    request.giveBudget(budget(60, employee), AT.plusHours(1));
    return request;
  }

  public static MaintenanceRequest approvedRequest(
      Integer id, Customer customer, Employee employee) {
    MaintenanceRequest request = budgetedRequest(id, customer, employee);
    request.approve(AT.plusHours(2), customer.getId());
    return request;
  }

  public static MaintenanceRequest fixedRequest(Integer id, Customer customer, Employee employee) {
    MaintenanceRequest request = approvedRequest(id, customer, employee);
    request.performMaintenance(maintenance(70, employee), AT.plusHours(3));
    return request;
  }

  public static MaintenanceRequest paidRequest(Integer id, Customer customer, Employee employee) {
    MaintenanceRequest request = fixedRequest(id, customer, employee);
    request.pay(AT.plusHours(4));
    return request;
  }

  public static MaintenanceRequest finishedRequest(
      Integer id, Customer customer, Employee employee) {
    MaintenanceRequest request = paidRequest(id, customer, employee);
    request.finish(employee, AT.plusHours(5));
    return request;
  }

  public static <T> T withId(T entity, Integer id) {
    ReflectionTestUtils.setField(entity, "id", id);
    return entity;
  }

  public static <T> T deactivate(T entity) {
    ReflectionTestUtils.setField(entity, "active", false);
    return entity;
  }
}
