import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
  provideRouter,
} from '@angular/router';
import { SessionService } from '../session.service';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let authenticated: boolean;

  const activate = () =>
    TestBed.runInInjectionContext(() =>
      authGuard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot),
    );

  beforeEach(() => {
    authenticated = true;

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: SessionService, useValue: { isAuthenticated: () => authenticated } },
      ],
    });
  });

  it('allows authenticated users', () => {
    expect(activate()).toBe(true);
  });

  it('redirects anonymous users to the login page', () => {
    authenticated = false;

    expect(TestBed.inject(Router).serializeUrl(activate() as UrlTree)).toBe('/login');
  });
});
