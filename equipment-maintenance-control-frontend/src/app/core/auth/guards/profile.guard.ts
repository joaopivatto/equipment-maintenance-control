import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ProfileType } from '../../../shared';
import { SessionService } from '../session.service';

function profileGuard(profileType: ProfileType): CanActivateFn {
  return () => {
    const sessionService = inject(SessionService);
    const router = inject(Router);

    if (!sessionService.isAuthenticated()) {
      return router.createUrlTree(['/login']);
    }

    return sessionService.profileType() === profileType || router.createUrlTree(['/requests/list']);
  };
}

export const employeeGuard: CanActivateFn = profileGuard(ProfileType.EMPLOYEE);

export const customerGuard: CanActivateFn = profileGuard(ProfileType.CUSTOMER);
