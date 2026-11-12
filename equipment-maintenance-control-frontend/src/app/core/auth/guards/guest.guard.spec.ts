import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
  provideRouter,
} from '@angular/router';
import { SessionService } from '..';
import { guestGuard } from './guest.guard';

describe('guestGuard', () => {
  let authenticated: boolean;

  const activate = () =>
    TestBed.runInInjectionContext(() =>
      guestGuard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot),
    );

  beforeEach(() => {
    authenticated = false;

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: SessionService, useValue: { isAuthenticated: () => authenticated } },
      ],
    });
  });

  it('allows anonymous visitors', () => {
    expect(activate()).toBe(true);
  });

  it('sends authenticated users back to the request list', () => {
    authenticated = true;

    expect(TestBed.inject(Router).serializeUrl(activate() as UrlTree)).toBe('/requests/list');
  });
});
