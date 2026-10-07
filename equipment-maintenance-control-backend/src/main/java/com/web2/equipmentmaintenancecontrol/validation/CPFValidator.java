package com.web2.equipmentmaintenancecontrol.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

  @Override
  public boolean isValid(String cpf, ConstraintValidatorContext context) {
    if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
      return false;
    }

    int firstCheckDigit = calculateCheckDigit(cpf, 9);
    int secondCheckDigit = calculateCheckDigit(cpf, 10);

    return firstCheckDigit == Character.getNumericValue(cpf.charAt(9))
        && secondCheckDigit == Character.getNumericValue(cpf.charAt(10));
  }

  private int calculateCheckDigit(String cpf, int digitsCount) {
    int weight = digitsCount + 1;
    int sum = 0;

    for (int i = 0; i < digitsCount; i++) {
      sum += Character.getNumericValue(cpf.charAt(i)) * weight;
      weight--;
    }

    int remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }
}
