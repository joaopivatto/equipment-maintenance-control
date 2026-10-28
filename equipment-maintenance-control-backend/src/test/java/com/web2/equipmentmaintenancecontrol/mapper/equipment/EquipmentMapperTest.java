package com.web2.equipmentmaintenancecontrol.mapper.equipment;

import static org.assertj.core.api.Assertions.assertThat;

import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentResponse;
import com.web2.equipmentmaintenancecontrol.model.equipment.dto.EquipmentTypeResponse;
import com.web2.equipmentmaintenancecontrol.support.Fixtures;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Mappers de equipamento")
class EquipmentMapperTest {

  private final EquipmentTypeMapper typeMapper = new EquipmentTypeMapper();
  private final EquipmentMapper equipmentMapper = new EquipmentMapper(typeMapper);

  @Nested
  @DisplayName("EquipmentTypeMapper")
  class TypeMapper {

    @Test
    @DisplayName("converte entidade em resposta")
    void mapsEntity() {
      EquipmentTypeResponse response = typeMapper.toResponse(Fixtures.equipmentType(3, "Notebook"));

      assertThat(response).isEqualTo(new EquipmentTypeResponse(3, "Notebook"));
    }

    @Test
    @DisplayName("converte entidade nula em null e lista nula em lista vazia")
    void handlesNulls() {
      assertThat(
              typeMapper.toResponse(
                  (com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType) null))
          .isNull();
      assertThat(
              typeMapper.toResponse(
                  (List<com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType>) null))
          .isEmpty();
    }

    @Test
    @DisplayName("converte lista preservando a ordem e os itens nulos")
    void mapsList() {
      assertThat(
              typeMapper.toResponse(
                  Arrays.asList(
                      Fixtures.equipmentType(1, "Notebook"),
                      null,
                      Fixtures.equipmentType(2, "PC"))))
          .containsExactly(
              new EquipmentTypeResponse(1, "Notebook"), null, new EquipmentTypeResponse(2, "PC"));
    }
  }

  @Nested
  @DisplayName("EquipmentMapper")
  class ItemMapper {

    @Test
    @DisplayName("converte equipamento aninhando o tipo")
    void mapsEntityWithNestedType() {
      EquipmentResponse response = equipmentMapper.toResponse(Fixtures.equipment(50));

      assertThat(response.id()).isEqualTo(50);
      assertThat(response.description()).isEqualTo("Notebook Dell Inspiron");
      assertThat(response.type()).isEqualTo(new EquipmentTypeResponse(7, "Notebook"));
    }

    @Test
    @DisplayName("converte entidade nula em null e lista nula em lista vazia")
    void handlesNulls() {
      assertThat(
              equipmentMapper.toResponse(
                  (com.web2.equipmentmaintenancecontrol.model.equipment.Equipment) null))
          .isNull();
      assertThat(
              equipmentMapper.toResponse(
                  (List<com.web2.equipmentmaintenancecontrol.model.equipment.Equipment>) null))
          .isEmpty();
    }

    @Test
    @DisplayName("converte lista de equipamentos")
    void mapsList() {
      assertThat(equipmentMapper.toResponse(List.of(Fixtures.equipment(1), Fixtures.equipment(2))))
          .extracting(EquipmentResponse::id)
          .containsExactly(1, 2);
    }
  }
}
