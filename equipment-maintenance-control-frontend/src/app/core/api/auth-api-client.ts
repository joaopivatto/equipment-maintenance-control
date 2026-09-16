import { Observable } from 'rxjs';
import { Address } from '../../shared';
import { SessionUser } from '../auth/models/session-user.model';
import { SignUpRequest } from '../auth/models/sign-up-request.model';

export abstract class AuthApiClient {
  abstract login(email: string, password: string): Observable<SessionUser | null>;

  abstract signUp(request: SignUpRequest): Observable<{ error?: string }>;

  abstract logout(): Observable<void>;

  abstract findAddressByZipCode(zipCode: string): Observable<Address | null>;
}
