import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { EMPTY, Observable, catchError, map, of } from 'rxjs';
import { Address } from '../../shared';
import { SessionUser } from '../auth/models/session-user.model';
import { SignUpRequest } from '../auth/models/sign-up-request.model';
import { NotificationService } from '../notifications/notification.service';
import { API_BASE_URL } from './api.config';
import { toErrorMessage } from './api-error';
import { AuthApiClient } from './auth-api-client';
import { ApiLoginResponseDto, ViaCepResponseDto } from './dto';
import { toAddress, toSessionUser } from './mappers/auth.mapper';

const onlyDigits = (value: string): string => value.replace(/\D/g, '');

@Injectable()
export class HttpAuthApiClient extends AuthApiClient {
  private readonly http = inject(HttpClient);
  private readonly notifications = inject(NotificationService);

  private readonly VIA_CEP_URL = 'https://viacep.com.br/ws';

  login(email: string, password: string): Observable<SessionUser | null> {
    return this.http
      .post<ApiLoginResponseDto>(`${API_BASE_URL}/auth/login`, { email, password })
      .pipe(
        map((dto) => toSessionUser(dto, email)),
        catchError((error: unknown) => {
          if (
            error instanceof HttpErrorResponse &&
            (error.status === 401 || error.status === 400)
          ) {
            return of(null);
          }

          this.notifications.error(
            'Erro',
            toErrorMessage(error, 'Não foi possível realizar o login. Tente novamente.'),
          );
          return EMPTY;
        }),
      );
  }

  signUp(request: SignUpRequest): Observable<{ error?: string }> {
    const body = {
      name: request.name.trim(),
      email: request.email.trim(),
      cpf: onlyDigits(request.cpf),
      phoneNumber: onlyDigits(request.phoneNumber),
      address: {
        street: request.address.street,
        number: request.address.number,
        complement: request.address.complement,
        neighborhood: request.address.neighborhood,
        city: request.address.city,
        state: request.address.state,
        zipCode: onlyDigits(request.address.zipCode),
      },
    };

    return this.http.post<void>(`${API_BASE_URL}/auth/signup`, body).pipe(
      map(() => ({}) as { error?: string }),
      catchError((error: unknown) =>
        of({ error: toErrorMessage(error, 'Não foi possível concluir o cadastro.') }),
      ),
    );
  }

  logout(): Observable<void> {
    return of(undefined);
  }

  findAddressByZipCode(zipCode: string): Observable<Address | null> {
    const sanitizedZipCode = onlyDigits(zipCode);

    return this.http.get<ViaCepResponseDto>(`${this.VIA_CEP_URL}/${sanitizedZipCode}/json/`).pipe(
      map((dto) => (dto.erro ? null : toAddress(dto))),
      catchError(() => of(null)),
    );
  }
}
