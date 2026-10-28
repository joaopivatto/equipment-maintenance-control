package com.web2.equipmentmaintenancecontrol.model.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Perfis (Customer / Employee)")
class ProfileTest {

  private static final HashedSaltedPassword PASSWORD = new HashedSaltedPassword("h", "s");
  private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 20);

  @Nested
  @DisplayName("Employee")
  class EmployeeTest {

    @Test
    @DisplayName("é criado ativo, com tipo EMPLOYEE e senha desmembrada em hash e salt")
    void createsActiveEmployee() {
      Employee employee = new Employee("Ana", "ana@web2.com", BIRTH_DATE, PASSWORD);

      assertThat(employee.getName()).isEqualTo("Ana");
      assertThat(employee.getEmail()).isEqualTo("ana@web2.com");
      assertThat(employee.getBirthDate()).isEqualTo(BIRTH_DATE);
      assertThat(employee.getType()).isEqualTo(ProfileType.EMPLOYEE);
      assertThat(employee.getPasswordHash()).isEqualTo("h");
      assertThat(employee.getPasswordSalt()).isEqualTo("s");
      assertThat(employee.getActive()).isTrue();
    }

    @Test
    @DisplayName("atualiza nome, e-mail e data de nascimento preservando a senha")
    void updatesBasicInfo() {
      Employee employee = new Employee("Ana", "ana@web2.com", BIRTH_DATE, PASSWORD);

      employee.update("Ana Maria", "ana.maria@web2.com", LocalDate.of(1991, 1, 2));

      assertThat(employee.getName()).isEqualTo("Ana Maria");
      assertThat(employee.getEmail()).isEqualTo("ana.maria@web2.com");
      assertThat(employee.getBirthDate()).isEqualTo(LocalDate.of(1991, 1, 2));
      assertThat(employee.getPasswordHash()).isEqualTo("h");
    }

    @Test
    @DisplayName("exige nome, e-mail, senha e data de nascimento")
    void requiresMandatoryFields() {
      assertThatThrownBy(() -> new Employee(null, "ana@web2.com", BIRTH_DATE, PASSWORD))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("name");

      assertThatThrownBy(() -> new Employee("Ana", null, BIRTH_DATE, PASSWORD))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("email");

      assertThatThrownBy(() -> new Employee("Ana", "ana@web2.com", BIRTH_DATE, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("password");

      assertThatThrownBy(() -> new Employee("Ana", "ana@web2.com", null, PASSWORD))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("birthDate");
    }

    @Test
    @DisplayName("recusa atualização com campos nulos")
    void rejectsNullOnUpdate() {
      Employee employee = new Employee("Ana", "ana@web2.com", BIRTH_DATE, PASSWORD);

      assertThatThrownBy(() -> employee.update(null, "ana@web2.com", BIRTH_DATE))
          .isInstanceOf(NullPointerException.class);
      assertThatThrownBy(() -> employee.update("Ana", "ana@web2.com", null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("Customer")
  class CustomerTest {

    @Test
    @DisplayName("é criado ativo, com tipo CUSTOMER, endereço e telefone")
    void createsActiveCustomer() {
      Customer customer =
          new Customer(
              "Bruno",
              "bruno@web2.com",
              PASSWORD,
              "12345678901",
              Fixtures.address(),
              new Phone("41999990000"));

      assertThat(customer.getType()).isEqualTo(ProfileType.CUSTOMER);
      assertThat(customer.getCpf()).isEqualTo("12345678901");
      assertThat(customer.getAddress().getCity()).isEqualTo("Curitiba");
      assertThat(customer.getPhone().getNumber()).isEqualTo("41999990000");
      assertThat(customer.getActive()).isTrue();
    }

    @Test
    @DisplayName("exige CPF, endereço e telefone")
    void requiresMandatoryFields() {
      assertThatThrownBy(
              () ->
                  new Customer(
                      "Bruno",
                      "bruno@web2.com",
                      PASSWORD,
                      null,
                      Fixtures.address(),
                      new Phone("41999990000")))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("cpf");

      assertThatThrownBy(
              () ->
                  new Customer(
                      "Bruno",
                      "bruno@web2.com",
                      PASSWORD,
                      "12345678901",
                      null,
                      new Phone("41999990000")))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("address");

      assertThatThrownBy(
              () ->
                  new Customer(
                      "Bruno", "bruno@web2.com", PASSWORD, "12345678901", Fixtures.address(), null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("phone");
    }
  }

  @Nested
  @DisplayName("Address e Phone")
  class AddressAndPhone {

    @Test
    @DisplayName("endereço exige CEP e rua, mas aceita complemento nulo")
    void addressValidation() {
      Address address = new Address("80010000", "Rua A", 10, null, "Centro", "Curitiba", "PR");
      assertThat(address.getComplement()).isNull();

      assertThatThrownBy(() -> new Address(null, "Rua A", 10, null, "Centro", "Curitiba", "PR"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("zipCode");

      assertThatThrownBy(() -> new Address("80010000", null, 10, null, "Centro", "Curitiba", "PR"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("street");
    }

    @Test
    @DisplayName("telefone exige número")
    void phoneValidation() {
      assertThat(new Phone("41999990000").getNumber()).isEqualTo("41999990000");

      assertThatThrownBy(() -> new Phone(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("number");
    }
  }
}
