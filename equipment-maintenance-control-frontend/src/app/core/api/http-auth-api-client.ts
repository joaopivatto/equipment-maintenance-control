import { inject } from '@angular/core';
import { AuthApiClient } from './auth-api-client';
import { HttpClient } from '@angular/common/http';
import { SignUpRequest } from '../auth/models/sign-up-request.model';
import { Observable, catchError, map, of } from 'rxjs';
import { SessionUser } from '../auth/models/session-user.model';
import { MockSessionUserFactory } from '../auth/models/testing/session-user.mock';
import { Address, ProfileType } from '../../shared';
import { ViaCepResponseDto } from './dto';
import { toAddress } from './mappers/auth.mapper';

interface MockAccount extends SessionUser {
  password: string;
  cpf?: string;
  phoneNumber?: string;
  address?: Address;
}

export class HttpAuthApiClient extends AuthApiClient {
  private readonly http = inject(HttpClient);
  private readonly VIA_CEP_URL = 'https://viacep.com.br/ws';

  private readonly accountFactory = new MockSessionUserFactory();

  private readonly accounts: MockAccount[] = [this.accountFactory.generate(), this.accountFactory.generate()];

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
    const sanitizedZipCode = zipCode.replace(/\D/g, '');

    return this.http.get<ViaCepResponseDto>(`${this.VIA_CEP_URL}/${sanitizedZipCode}/json/`).pipe(
      map((dto) => (dto.erro ? null : toAddress(dto))),
      catchError(() => of(null)),
    );
  }

  private generatePassword(): string {
    return String(Math.floor(1000 + Math.random() * 9000));
  }
}
