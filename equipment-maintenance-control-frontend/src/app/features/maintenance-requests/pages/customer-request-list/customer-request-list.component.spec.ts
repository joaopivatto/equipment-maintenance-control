import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MockMaintenanceRequestApiClient } from '../../api/mock-maintenance-request-api-client';

import { CustomerRequestListComponent } from './customer-request-list.component';

describe('CustomerRequestListComponent', () => {
  let component: CustomerRequestListComponent;
  let fixture: ComponentFixture<CustomerRequestListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerRequestListComponent],
      providers: [
        provideRouter([]),
        MessageService,
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerRequestListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
