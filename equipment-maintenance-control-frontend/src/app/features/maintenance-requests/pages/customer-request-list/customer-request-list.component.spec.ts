import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Router, provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Observable, of } from 'rxjs';
import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MockMaintenanceRequestApiClient } from '../../api/mock-maintenance-request-api-client';
import { EmployeeApiClient } from '../../../employees/api/employee-api-client';
import { MockEmployeeApiClient } from '../../../employees/api/mock-employee-api-client';
import { AuthApiClient } from '../../../../core/api/auth-api-client';
import { MockAuthApiClient } from '../../../../core/api/mock-auth-api-client';

import { SessionService } from '../../../../core/auth/session.service';
import { ProfileType } from '../../../../shared';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import { MaintenanceRequestMockFactory } from '../../models/testing/maintenance-request.mock';

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
        { provide: EmployeeApiClient, useClass: MockEmployeeApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
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

describe('CustomerRequestListComponent as an employee', () => {
  let component: CustomerRequestListComponent;
  let fixture: ComponentFixture<CustomerRequestListComponent>;
  let router: Router;
  let openRequest: MaintenanceRequest;

  const employee = {
    id: 1,
    name: 'Matheus Smith',
    email: 'matheus@email.com',
    profileType: ProfileType.EMPLOYEE,
  };

  beforeEach(async () => {
    openRequest = {
      ...new MaintenanceRequestMockFactory().generate(),
      id: 42,
      status: RequestStatus.OPEN,
      createdAt: new Date().toISOString(),
    };

    await TestBed.configureTestingModule({
      imports: [CustomerRequestListComponent],
      providers: [
        provideRouter([]),
        MessageService,
        {
          provide: MaintenanceRequestApiClient,
          useValue: {
            listAll: (): Observable<MaintenanceRequest[]> => of([openRequest]),
          },
        },
        { provide: EmployeeApiClient, useClass: MockEmployeeApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
        {
          provide: SessionService,
          useValue: {
            currentUser: () => employee,
            isEmployee: () => true,
            isCustomer: () => false,
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerRequestListComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    await fixture.whenStable();
    fixture.detectChanges();
  });

  it('opens the budget page when the employee clicks "Orçar"', () => {
    const navigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);

    const budgetButton = fixture.debugElement
      .queryAll(By.css('button'))
      .find((button) => (button.nativeElement as HTMLElement).textContent?.includes('Orçar'));

    expect(budgetButton).toBeTruthy();

    budgetButton!.nativeElement.click();

    expect(navigate).toHaveBeenCalledWith(['/requests', openRequest.id, 'budget']);
  });

  it('keeps performBudget available to employees', () => {
    const navigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);

    component.performBudget(7);

    expect(navigate).toHaveBeenCalledWith(['/requests', 7, 'budget']);
  });
});
