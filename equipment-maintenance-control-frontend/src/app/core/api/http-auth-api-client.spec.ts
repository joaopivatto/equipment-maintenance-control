import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MessageService } from 'primeng/api';
import { ProfileType } from '../../shared';
import { HttpAuthApiClient } from './http-auth-api-client';

describe('HttpAuthApiClient', () => {
  let client: HttpAuthApiClient;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        HttpAuthApiClient,
        MessageService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    client = TestBed.inject(HttpAuthApiClient);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('converte a resposta do login em usuário de sessão', () => {
    let result: unknown;
    client.login('ana@x.com', '1234').subscribe((user) => (result = user));

    const req = http.expectOne('/api/auth/login');
    expect(req.request.body).toEqual({ email: 'ana@x.com', password: '1234' });
    req.flush({ userId: 7, profileType: 'EMPLOYEE', name: 'Ana', email: 'ana@x.com' });

    expect(result).toEqual({
      id: 7,
      name: 'Ana',
      email: 'ana@x.com',
      profileType: ProfileType.EMPLOYEE,
    });
  });

  it('devolve null quando as credenciais são inválidas', () => {
    let result: unknown = 'pendente';
    client.login('ana@x.com', 'errada').subscribe((user) => (result = user));

    http
      .expectOne('/api/auth/login')
      .flush({ code: 'INVALID_CREDENTIALS' }, { status: 401, statusText: 'Unauthorized' });

    expect(result).toBeNull();
  });
});
