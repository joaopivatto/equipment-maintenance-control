import { Injectable, inject, signal, computed } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { ProfileType } from '../../shared';
import { AuthApiClient } from '../api/auth-api-client';
import { SessionUser } from './models/session-user.model';

const STORAGE_KEY = 'session-user';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly authApiClient = inject(AuthApiClient);
  private readonly currentUserState = signal<SessionUser | null>(this.readStoredUser());

  readonly currentUser = this.currentUserState.asReadonly();

  readonly isAuthenticated = computed(() => this.currentUser() !== null);

  readonly profileType = computed(() => this.currentUser()?.profileType ?? null);

  readonly isEmployee = computed(() => this.profileType() === ProfileType.EMPLOYEE);

  readonly isCustomer = computed(() => this.profileType() === ProfileType.CUSTOMER);

  login(email: string, password: string): Observable<ProfileType | null> {
    return this.authApiClient.login(email, password).pipe(
      tap((user) => this.setCurrentUser(user)),
      map((user) => user?.profileType ?? null),
    );
  }

  logout(): void {
    this.authApiClient.logout().subscribe(() => this.setCurrentUser(null));
  }

  private setCurrentUser(user: SessionUser | null): void {
    this.currentUserState.set(user);
    if (user) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }

  private readStoredUser(): SessionUser | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as SessionUser;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
