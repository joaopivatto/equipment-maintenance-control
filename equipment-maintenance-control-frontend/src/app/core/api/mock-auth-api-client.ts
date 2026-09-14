import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Address, ProfileType } from '../../shared';
import { AddressMockFactory } from '../../shared/testing';
import { SessionUser } from '../auth/models/session-user.model';
import { SignUpRequest } from '../auth/models/sign-up-request.model';
import { MockSessionUserFactory } from '../auth/models/testing/session-user.mock';
import { AuthApiClient } from './auth-api-client';

interface MockAccount extends SessionUser {
  password: string;
  cpf?: string;
  phoneNumber?: string;
  address?: Address;
}

@Injectable()
export class MockAuthApiClient extends AuthApiClient {
  private readonly accountFactory = new MockSessionUserFactory();
  private readonly addressFactory = new AddressMockFactory();

  private readonly accounts: MockAccount[] = [
    this.accountFactory.generate(),
    this.accountFactory.generate(),
  ];

  private nextId = this.accounts.length + 1;

  login(email: string, password: string): Observable<SessionUser | null> {
    const account = this.accounts.find((acc) => acc.email === email && acc.password === password);
    if (!account) {
      return of(null);
    }

    const { password: _password, ...user } = account;
    return of(user);
  }

  signUp(request: SignUpRequest): Observable<{ error?: string }> {
    const emailTaken = this.accounts.some((acc) => acc.email === request.email);
    if (emailTaken) {
      return of({ error: 'Já existe uma conta cadastrada com este e-mail.' });
    }

    const account: MockAccount = {
      id: this.nextId++,
      name: request.name,
      email: request.email,
      // Senha gerada e enviada por e-mail; nunca volta na resposta da API.
      password: this.generatePassword(),
      profileType: ProfileType.CUSTOMER,
      cpf: request.cpf,
      phoneNumber: request.phoneNumber,
      address: request.address,
    };
    this.accounts.push(account);

    return of({});
  }

  logout(): Observable<void> {
    return of(undefined);
  }

  findAddressByZipCode(zipCode: string): Observable<Address | null> {
    return of(this.addressFactory.generate({ zipCode }));
  }

  private generatePassword(): string {
    return String(Math.floor(1000 + Math.random() * 9000));
  }
}
