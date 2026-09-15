import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MockMaintenanceRequestApiClient } from '../../api/mock-maintenance-request-api-client';
import { AuthApiClient } from '../../../../core/api/auth-api-client';
import { MockAuthApiClient } from '../../../../core/api/mock-auth-api-client';
import { RequestDetailsComponent } from './request-details.component';

describe('RequestDetailsComponent', () => {
  let component: RequestDetailsComponent;
  let fixture: ComponentFixture<RequestDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestDetailsComponent],
      providers: [
        MessageService,
        provideRouter([]),
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: '1' } } } },
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RequestDetailsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
