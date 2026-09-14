import { Injectable, inject } from '@angular/core';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Observable } from 'rxjs';
import { Address } from '../../../shared';
import { AuthApiClient } from '../../../core/api/auth-api-client';

@Injectable({
  providedIn: 'root',
})
export class SignUpService {
  public cpfValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      return this.isValidCpf(control.value) ? null : { cpf: true };
    };
  }

  public emailValidator(controlName: string, matchingControlName: string): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
      const control = group.get(controlName);
      const matchingControl = group.get(matchingControlName);

      if (!control || !matchingControl || control.value === matchingControl.value) {
        return null;
      }

      return { mismatch: true };
    };
  }

  private isValidCpf(cpf: string): boolean {
    const digits = cpf.replace(/\D/g, '');

    if (digits.length !== 11 || /^(\d)\1{10}$/.test(digits)) {
      return false;
    }

    const firstCheckDigit = this.calculateCheckDigit(digits.slice(0, 9), 10);
    const secondCheckDigit = this.calculateCheckDigit(digits.slice(0, 9) + firstCheckDigit, 11);

    return Number(digits[9]) === firstCheckDigit && Number(digits[10]) === secondCheckDigit;
  }

  private calculateCheckDigit(digits: string, startingFactor: number): number {
    let sum = 0;
    let factor = startingFactor;

    for (const digit of digits) {
      sum += Number(digit) * factor--;
    }

    const remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }
}
