import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
  UrlTree,
  provideRouter,
} from '@angular/router';
import { ProfileType } from '../../../shared';
import { SessionService } from '..';
import { customerGuard, employeeGuard } from './profile.guard';

describe('profileGuard', () => {
  let profileType: ProfileType | null;

  const activate = (guard: CanActivateFn) =>
    TestBed.runInInjectionContext(() =>
      guard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot),
    );

  const redirectOf = (guard: CanActivateFn) =>
    TestBed.inject(Router).serializeUrl(activate(guard) as UrlTree);

  beforeEach(() => {
    profileType = ProfileType.EMPLOYEE;

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        {
          provide: SessionService,
          useValue: {
            isAuthenticated: () => profileType !== null,
            profileType: () => profileType,
          },
        },
      ],
    });
  });

  it('allows each profile on its own routes', () => {
    expect(activate(employeeGuard)).toBe(true);

    profileType = ProfileType.CUSTOMER;
    expect(activate(customerGuard)).toBe(true);
  });

  it('sends a profile that does not match to the request list', () => {
    expect(redirectOf(customerGuard)).toBe('/requests/list');

    profileType = ProfileType.CUSTOMER;
    expect(redirectOf(employeeGuard)).toBe('/requests/list');
  });

  it('redirects anonymous users to the login page', () => {
    profileType = null;

    expect(redirectOf(employeeGuard)).toBe('/login');
    expect(redirectOf(customerGuard)).toBe('/login');
  });
});
