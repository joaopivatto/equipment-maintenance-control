package com.web2.equipmentmaintenancecontrol.mapper.profile;

import static org.assertj.core.api.Assertions.assertThat;

import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Employee;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.CustomerResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Mappers de perfil")
class ProfileMapperTest {

  private final EmployeeMapper employeeMapper = new EmployeeMapper();
  private final CustomerMapper customerMapper = new CustomerMapper();

  @Nested
  @DisplayName("EmployeeMapper")
  class EmployeeMapping {

    @Test
    @DisplayName("expõe id, nome, e-mail e data de nascimento, sem a senha")
    void mapsEntity() {
      EmployeeResponse response = employeeMapper.toResponse(Fixtures.employee(10, "Ana"));

      assertThat(response)
          .isEqualTo(new EmployeeResponse(10, "Ana", "func10@web2.com", LocalDate.of(1995, 6, 15)));
    }

    @Test
    @DisplayName("converte entidade nula em null e lista nula em lista vazia")
    void handlesNulls() {
      assertThat(employeeMapper.toResponse((Employee) null)).isNull();
      assertThat(employeeMapper.toResponse((List<Employee>) null)).isEmpty();
    }

    @Test
    @DisplayName("converte lista preservando a ordem")
    void mapsList() {
      assertThat(
              employeeMapper.toResponse(
                  List.of(Fixtures.employee(1, "Ana"), Fixtures.employee(2, "Bia"))))
          .extracting(EmployeeResponse::name)
          .containsExactly("Ana", "Bia");
    }
  }

  @Nested
  @DisplayName("CustomerMapper")
  class CustomerMapping {

    @Test
    @DisplayName("expõe apenas id, nome e e-mail (sem CPF, endereço ou senha)")
    void mapsEntity() {
      CustomerResponse response = customerMapper.toResponse(Fixtures.customer(5, "Bruno"));

      assertThat(response).isEqualTo(new CustomerResponse(5, "Bruno", "cliente5@web2.com"));
    }

    @Test
    @DisplayName("converte entidade nula em null e lista nula em lista vazia")
    void handlesNulls() {
      assertThat(customerMapper.toResponse((Customer) null)).isNull();
      assertThat(customerMapper.toResponse((List<Customer>) null)).isEmpty();
    }

    @Test
    @DisplayName("converte lista preservando a ordem")
    void mapsList() {
      assertThat(
              customerMapper.toResponse(
                  List.of(Fixtures.customer(1, "Ana"), Fixtures.customer(2, "Bia"))))
          .extracting(CustomerResponse::name)
          .containsExactly("Ana", "Bia");
    }
  }
}
