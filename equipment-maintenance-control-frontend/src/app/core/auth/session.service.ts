import { Service, signal, computed } from '@angular/core';
import { ProfileType } from '../../shared';
import { SessionUser } from './models/session-user.model';

interface MockAccount extends SessionUser {
  password: string;
}

@Service()
export class SessionService {
  private readonly mockAccounts: MockAccount[] = [
  {
    id: 1,
    name: 'João da Silva',
    email: 'joao@cliente.com',
    password: '1234',
    profileType: ProfileType.CUSTOMER,
  },
  {
    id: 2,
    name: 'Maria da Costa',
    email: 'maria@empresa.com',
    password: '1234',
    profileType: ProfileType.EMPLOYEE,
  },
];
  private readonly currentUserState = signal<SessionUser | null>(null);

  readonly currentUser = this.currentUserState.asReadonly();

  readonly isAuthenticated = computed(
    () => this.currentUser() !== null,
  );

  readonly profileType = computed(
    () => this.currentUser()?.profileType ?? null,
  );

  login(email: string, password: string): boolean {
    const account = this.mockAccounts.find(
      (acc) => acc.email === email && acc.password === password,
    );
    if (account) {
      this.currentUserState.set({
        id: account.id,
        name: account.name,
        email: account.email,
        profileType: account.profileType,
      });
      return true;
    }
    return false;
  }

  logout(): void {
    this.currentUserState.set(null);
  }
}
