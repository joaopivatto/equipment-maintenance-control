import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { API_BASE_URL } from '../api/api.config';
import { SessionService } from './session.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(SessionService).currentUser()?.token;
  const isApiRequest = request.url.startsWith(`${API_BASE_URL}/`);
  const isPublicRequest =
    request.url === `${API_BASE_URL}/auth/login` ||
    request.url === `${API_BASE_URL}/auth/signup`;

  if (token && isApiRequest && !isPublicRequest) {
    return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
  }

  return next(request);
};
