import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { EMPTY, catchError, throwError } from 'rxjs';
import { API_BASE_URL } from '../api/api.config';
import { NotificationService } from '../notifications/notification.service';
import { SessionService } from './session.service';

const PUBLIC_URLS = [`${API_BASE_URL}/auth/login`, `${API_BASE_URL}/auth/signup`];

export const authErrorInterceptor: HttpInterceptorFn = (request, next) => {
  const session = inject(SessionService);
  const router = inject(Router);
  const notifications = inject(NotificationService);

  return next(request).pipe(
    catchError((error: unknown) => {
      const isProtectedApiCall =
        request.url.startsWith(`${API_BASE_URL}/`) && !PUBLIC_URLS.includes(request.url);

      if (!(error instanceof HttpErrorResponse) || error.status !== 401 || !isProtectedApiCall) {
        return throwError(() => error);
      }

      if (session.isAuthenticated()) {
        session.logout().subscribe(() => {
          notifications.error('Sessão expirada', 'Faça login novamente para continuar.');
          void router.navigate(['/login']);
        });
      }

      return EMPTY;
    }),
  );
};
