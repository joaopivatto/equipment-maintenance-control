import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ProfileType } from '../../shared';
import { SessionUser } from '../auth/models/session-user.model';
import { MockSessionUserFactory } from '../auth/models/testing/session-user.mock';
import { AuthApiClient } from './auth-api-client';

interface MockAccount extends SessionUser {
  password: string;
}

@Injectable()
export class MockAuthApiClient extends AuthApiClient {
  private readonly accountFactory = new MockSessionUserFactory();

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

  signUp(name: string, email: string, password: string): Observable<SessionUser> {
    const account: MockAccount = {
      id: this.nextId++,
      name,
      email,
      password,
      profileType: ProfileType.CUSTOMER,
    };
    this.accounts.push(account);

    const { password: _password, ...user } = account;
    return of(user);
  }

  logout(): Observable<void> {
    return of(undefined);
  }
}
