package com.web2.equipmentmaintenancecontrol.model.equipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Equipamentos")
class EquipmentTest {

  @Nested
  @DisplayName("EquipmentType")
  class Type {

    @Test
    @DisplayName("é criado ativo com a descrição informada")
    void createsActiveType() {
      EquipmentType type = new EquipmentType("Notebook");

      assertThat(type.getDescription()).isEqualTo("Notebook");
      assertThat(type.getActive()).isTrue();
    }

    @Test
    @DisplayName("atualiza a descrição")
    void updatesDescription() {
      EquipmentType type = new EquipmentType("Notebook");

      type.updateDescription("Notebook gamer");

      assertThat(type.getDescription()).isEqualTo("Notebook gamer");
    }

    @Test
    @DisplayName("exige descrição na criação e na atualização")
    void requiresDescription() {
      assertThatThrownBy(() -> new EquipmentType(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("description");

      EquipmentType type = new EquipmentType("Notebook");
      assertThatThrownBy(() -> type.updateDescription(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("description");
    }
  }

  @Nested
  @DisplayName("Equipment")
  class Item {

    @Test
    @DisplayName("é criado ativo, vinculado a um tipo")
    void createsActiveEquipment() {
      EquipmentType type = new EquipmentType("Notebook");
      Equipment equipment = new Equipment("Dell Inspiron", type);

      assertThat(equipment.getDescription()).isEqualTo("Dell Inspiron");
      assertThat(equipment.getType()).isSameAs(type);
      assertThat(equipment.getActive()).isTrue();
    }

    @Test
    @DisplayName("atualiza descrição e tipo")
    void updatesEquipment() {
      Equipment equipment = new Equipment("Dell Inspiron", new EquipmentType("Notebook"));
      EquipmentType newType = new EquipmentType("Desktop");

      equipment.update("PC montado", newType);

      assertThat(equipment.getDescription()).isEqualTo("PC montado");
      assertThat(equipment.getType()).isSameAs(newType);
    }

    @Test
    @DisplayName("exige descrição e tipo")
    void requiresDescriptionAndType() {
      EquipmentType type = new EquipmentType("Notebook");

      assertThatThrownBy(() -> new Equipment(null, type))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("description");

      assertThatThrownBy(() -> new Equipment("Dell Inspiron", null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("type");

      Equipment equipment = new Equipment("Dell Inspiron", type);
      assertThatThrownBy(() -> equipment.update("PC", null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("type");
    }
  }
}
