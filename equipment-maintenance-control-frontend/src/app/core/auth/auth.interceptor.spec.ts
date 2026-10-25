import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { authInterceptor } from './auth.interceptor';
import { SessionService } from './session.service';

describe('authInterceptor', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
  let token: string | undefined;

  beforeEach(() => {
    token = 'test-token';
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        { provide: SessionService, useValue: { currentUser: () => ({ token }) } },
      ],
    });
    http = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach(() => controller.verify());

  it('authenticates protected API calls', () => {
    http.get('/api/maintenance-request').subscribe();
    const request = controller.expectOne('/api/maintenance-request');
    expect(request.request.headers.get('Authorization')).toBe('Bearer test-token');
    request.flush([]);
  });

  it('does not send the token to public endpoints or external services', () => {
    for (const url of ['/api/auth/login', '/api/auth/signup', 'https://viacep.com.br/ws/80010000/json/']) {
      http.get(url).subscribe();
      const request = controller.expectOne(url);
      expect(request.request.headers.has('Authorization')).toBe(false);
      request.flush({});
    }
  });

  it('allows requests without a stored token', () => {
    token = undefined;
    http.get('/api/maintenance-request').subscribe();
    const request = controller.expectOne('/api/maintenance-request');
    expect(request.request.headers.has('Authorization')).toBe(false);
    request.flush([]);
  });
});
