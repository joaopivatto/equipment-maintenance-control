import { Injectable, inject, signal, computed } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { ProfileType } from '../../shared';
import { AuthApiClient } from '../api/auth-api-client';
import { SessionUser } from './models/session-user.model';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly authApiClient = inject(AuthApiClient);
  private readonly currentUserState = signal<SessionUser | null>(null);

  readonly currentUser = this.currentUserState.asReadonly();

  readonly isAuthenticated = computed(() => this.currentUser() !== null);

  readonly profileType = computed(() => this.currentUser()?.profileType ?? null);

  readonly isEmployee = computed(() => this.profileType() === ProfileType.EMPLOYEE);

  readonly isCustomer = computed(() => this.profileType() === ProfileType.CUSTOMER);

  login(email: string, password: string): Observable<ProfileType | null> {
    return this.authApiClient.login(email, password).pipe(
      tap((user) => this.currentUserState.set(user)),
      map((user) => user?.profileType ?? null),
    );
  }

  logout(): void {
    this.authApiClient.logout().subscribe(() => this.currentUserState.set(null));
  }
}
