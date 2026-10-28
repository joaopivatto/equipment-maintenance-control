package com.web2.equipmentmaintenancecontrol.service.equipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EquipmentService")
class EquipmentServiceTest {

  @Mock private EquipmentTypeService equipmentTypeService;

  @InjectMocks private EquipmentService service;

  @Test
  @DisplayName("cria equipamento resolvendo o tipo pelo id")
  void createsEquipmentResolvingType() {
    EquipmentType type = Fixtures.equipmentType(7, "Notebook");
    when(equipmentTypeService.findEntityById(7)).thenReturn(type);

    Equipment equipment = service.newEquipment("Dell Inspiron", 7);

    assertThat(equipment.getDescription()).isEqualTo("Dell Inspiron");
    assertThat(equipment.getType()).isSameAs(type);
    assertThat(equipment.getActive()).isTrue();
    assertThat(equipment.getId()).isNull();
  }

  @Test
  @DisplayName("propaga EQUIPMENT_TYPE_NOT_FOUND quando o tipo não existe")
  void propagatesTypeNotFound() {
    when(equipmentTypeService.findEntityById(99))
        .thenThrow(new AppException(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND));

    assertThatThrownBy(() -> service.newEquipment("Dell Inspiron", 99))
        .isInstanceOf(AppException.class)
        .extracting(exception -> ((AppException) exception).getErrorCode())
        .isEqualTo(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND);
  }
}
