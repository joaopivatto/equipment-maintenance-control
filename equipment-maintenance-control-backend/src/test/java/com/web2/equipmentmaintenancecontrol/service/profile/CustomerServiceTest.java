package com.web2.equipmentmaintenancecontrol.service.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.repository.profile.CustomerRepository;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService")
class CustomerServiceTest {

  @Mock private CustomerRepository repository;

  @InjectMocks private CustomerService service;

  @Test
  @DisplayName("busca cliente por id")
  void findsById() {
    Customer customer = Fixtures.customer(5);
    when(repository.findById(5)).thenReturn(Optional.of(customer));

    assertThat(service.findById(5)).isSameAs(customer);
  }

  @Test
  @DisplayName("falha com CUSTOMER_NOT_FOUND detalhando o id procurado")
  void failsWhenNotFound() {
    when(repository.findById(99)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.findById(99))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("99")
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.CUSTOMER_NOT_FOUND);
  }
}
