import { Observable } from 'rxjs';
import { SessionUser } from '../auth/models/session-user.model';

export abstract class AuthApiClient {
  abstract login(email: string, password: string): Observable<SessionUser | null>;

  abstract signUp(name: string, email: string, password: string): Observable<SessionUser>;

  abstract logout(): Observable<void>;
}
