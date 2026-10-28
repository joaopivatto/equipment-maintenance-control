package com.web2.equipmentmaintenancecontrol.domain.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("SecureRandomPasswordGenerator")
class SecureRandomPasswordGeneratorTest {

  private final SecureRandomPasswordGenerator generator = new SecureRandomPasswordGenerator();

  @ParameterizedTest(name = "gera senha com {0} dígitos")
  @ValueSource(ints = {0, 1, 4, 8})
  void generatesRequestedLength(int digits) {
    assertThat(generator.generate(digits)).hasSize(digits);
  }

  @Test
  @DisplayName("gera apenas dígitos numéricos")
  void generatesOnlyDigits() {
    IntStream.range(0, 50).forEach(i -> assertThat(generator.generate(4)).matches("\\d{4}"));
  }

  @Test
  @DisplayName("gera valores variados entre chamadas")
  void generatesVariedValues() {
    long distinct = IntStream.range(0, 50).mapToObj(i -> generator.generate(4)).distinct().count();

    assertThat(distinct).isGreaterThan(1);
  }
}
