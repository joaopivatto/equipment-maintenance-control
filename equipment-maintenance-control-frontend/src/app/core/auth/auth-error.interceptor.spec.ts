import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { NotificationService } from '../notifications/notification.service';
import { authErrorInterceptor } from './auth-error.interceptor';
import { SessionService } from './session.service';

describe('authErrorInterceptor', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
  let authenticated: boolean;
  let logout: ReturnType<typeof vi.fn>;
  let navigate: ReturnType<typeof vi.spyOn>;
  let notifyError: ReturnType<typeof vi.spyOn>;

  beforeEach(() => {
    authenticated = true;
    logout = vi.fn(() => of(undefined));

    TestBed.configureTestingModule({
      providers: [
        MessageService,
        provideRouter([]),
        provideHttpClient(withInterceptors([authErrorInterceptor])),
        provideHttpClientTesting(),
        { provide: SessionService, useValue: { isAuthenticated: () => authenticated, logout } },
      ],
    });

    http = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
    notifyError = vi.spyOn(TestBed.inject(NotificationService), 'error');
  });

  afterEach(() => controller.verify());

  it('ends the session and goes to the login page when the token is rejected', () => {
    const next = vi.fn();
    const failure = vi.fn();
    const complete = vi.fn();

    http.get('/api/maintenance-request').subscribe({ next, error: failure, complete });
    controller
      .expectOne('/api/maintenance-request')
      .flush({ code: 'INVALID_OR_EXPIRED_TOKEN' }, { status: 401, statusText: 'Unauthorized' });

    expect(logout).toHaveBeenCalledTimes(1);
    expect(notifyError).toHaveBeenCalledTimes(1);
    expect(navigate).toHaveBeenCalledWith(['/login']);
    expect(next).not.toHaveBeenCalled();
    expect(failure).not.toHaveBeenCalled();
    expect(complete).toHaveBeenCalled();
  });

  it('does not end the session again when nobody is logged in', () => {
    authenticated = false;

    http.get('/api/maintenance-request').subscribe();
    controller
      .expectOne('/api/maintenance-request')
      .flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(logout).not.toHaveBeenCalled();
    expect(navigate).not.toHaveBeenCalled();
  });

  it('lets the login call handle its own 401', () => {
    const failure = vi.fn();

    http.post('/api/auth/login', {}).subscribe({ error: failure });
    controller
      .expectOne('/api/auth/login')
      .flush({ code: 'INVALID_CREDENTIALS' }, { status: 401, statusText: 'Unauthorized' });

    expect(failure).toHaveBeenCalledTimes(1);
    expect(logout).not.toHaveBeenCalled();
  });

  it('passes other errors on to the caller', () => {
    const failure = vi.fn();

    http.get('/api/maintenance-request').subscribe({ error: failure });
    controller
      .expectOne('/api/maintenance-request')
      .flush({ code: 'INTERNAL_ERROR' }, { status: 500, statusText: 'Server Error' });

    expect(failure).toHaveBeenCalledTimes(1);
    expect(logout).not.toHaveBeenCalled();
  });
});
