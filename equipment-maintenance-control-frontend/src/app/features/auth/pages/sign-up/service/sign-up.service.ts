import { Injectable } from '@angular/core';
import { Address, Customer } from '../../../../../shared';

@Injectable({ providedIn: 'root' })
export class SignUpService {
  public async signUp(request: Customer): Promise<boolean> {
    const isSuccessful = true; // mock return for any user

    return isSuccessful;
  }

  public async getAddressByZipCode(zipCode: string): Promise<Address> {
    // mock return for any address
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          street: 'Rua do Teste',
          number: '0',
          complement: 'Fundos',
          neighborhood: 'Bairro do Teste',
          city: 'Curitiba',
          state: 'PR',
          zipCode,
        } as Address);
      }, 1000);
    });
  }

  public emailIsValid(email: string, confirmEmail: string): boolean {
    return email === confirmEmail;
  }

  public cpfIsValid(cpf: string): boolean {
    cpf = String(cpf).replace(/\D/g, '');

    if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) {
      return false;
    }

    const dv = (slice: string) => {
      let sum = 0;
      for (let i = 0; i < slice.length; i++) {
        sum += +slice[i] * (slice.length + 1 - i);
      }
      const rest = (sum * 10) % 11;
      return rest === 10 ? 0 : rest;
    };

    return dv(cpf.slice(0, 9)) === +cpf[9] && dv(cpf.slice(0, 10)) === +cpf[10];
  }
}
