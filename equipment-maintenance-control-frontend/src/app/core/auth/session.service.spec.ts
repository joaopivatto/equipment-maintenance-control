import { TestBed } from '@angular/core/testing';
import { AuthApiClient, MockAuthApiClient } from '../api';
import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [{ provide: AuthApiClient, useClass: MockAuthApiClient }],
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
