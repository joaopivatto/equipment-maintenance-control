package com.web2.equipmentmaintenancecontrol.service.equipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.mapper.equipment.EquipmentTypeMapper;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeRequest;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeResponse;
import com.web2.equipmentmaintenancecontrol.repository.equipment.EquipmentTypeRepository;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EquipmentTypeService")
class EquipmentTypeServiceTest {

  @Mock private EquipmentTypeRepository repository;

  private EquipmentTypeService service;

  @BeforeEach
  void setUp() {
    service = new EquipmentTypeService(repository, new EquipmentTypeMapper());
  }

  @Test
  @DisplayName("busca tipo por id")
  void findsById() {
    when(repository.findById(3)).thenReturn(Optional.of(Fixtures.equipmentType(3, "Notebook")));

    EquipmentTypeResponse response = service.findById(3);

    assertThat(response.id()).isEqualTo(3);
    assertThat(response.description()).isEqualTo("Notebook");
  }

  @Test
  @DisplayName("falha com EQUIPMENT_TYPE_NOT_FOUND quando o id não existe")
  void failsWhenNotFound() {
    when(repository.findById(99)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.findById(99))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND);
  }

  @Test
  @DisplayName("lista todos os tipos ativos")
  void listsAll() {
    when(repository.findAll())
        .thenReturn(
            List.of(
                Fixtures.equipmentType(1, "Notebook"), Fixtures.equipmentType(2, "Impressora")));

    List<EquipmentTypeResponse> responses = service.findAll();

    assertThat(responses)
        .extracting(EquipmentTypeResponse::description)
        .containsExactly("Notebook", "Impressora");
  }

  @Test
  @DisplayName("devolve lista vazia quando não há tipos")
  void listsEmpty() {
    when(repository.findAll()).thenReturn(List.of());

    assertThat(service.findAll()).isEmpty();
  }

  @Test
  @DisplayName("cria tipo a partir da descrição informada")
  void createsType() {
    when(repository.save(any(EquipmentType.class)))
        .thenAnswer(invocation -> Fixtures.withId(invocation.getArgument(0), 10));

    EquipmentTypeResponse response = service.create(new EquipmentTypeRequest("Impressora"));

    ArgumentCaptor<EquipmentType> captor = ArgumentCaptor.forClass(EquipmentType.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getDescription()).isEqualTo("Impressora");
    assertThat(captor.getValue().getActive()).isTrue();
    assertThat(response.id()).isEqualTo(10);
  }

  @Test
  @DisplayName("atualiza a descrição do tipo existente")
  void updatesType() {
    EquipmentType existing = Fixtures.equipmentType(4, "Notebook");
    when(repository.findById(4)).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);

    EquipmentTypeResponse response = service.update(4, new EquipmentTypeRequest("Notebook gamer"));

    assertThat(existing.getDescription()).isEqualTo("Notebook gamer");
    assertThat(response.description()).isEqualTo("Notebook gamer");
  }

  @Test
  @DisplayName("não atualiza tipo inexistente")
  void failsToUpdateMissingType() {
    when(repository.findById(99)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.update(99, new EquipmentTypeRequest("Qualquer")))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND);

    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("inativa o tipo (exclusão lógica)")
  void deletesType() {
    EquipmentType existing = Fixtures.equipmentType(4);
    when(repository.findById(4)).thenReturn(Optional.of(existing));

    service.delete(4);

    verify(repository).delete(existing);
  }

  @Test
  @DisplayName("falha ao inativar tipo já inativo")
  void failsToDeleteInactiveType() {
    EquipmentType existing = Fixtures.deactivate(Fixtures.equipmentType(4));
    when(repository.findById(4)).thenReturn(Optional.of(existing));

    assertThatThrownBy(() -> service.delete(4))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.EQUIPMENT_TYPE_ALREADY_INACTIVE);

    verify(repository, never()).delete(any(EquipmentType.class));
  }

  @Test
  @DisplayName("findEntityById devolve a entidade de domínio para outros serviços")
  void findsEntityById() {
    EquipmentType existing = Fixtures.equipmentType(4);
    when(repository.findById(4)).thenReturn(Optional.of(existing));

    assertThat(service.findEntityById(4)).isSameAs(existing);
  }
}
