import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MaintenanceRequestApiClient, MockMaintenanceRequestApiClient } from '../../api';
import { AuthApiClient, MockAuthApiClient } from '../../../../core/api';
import { BudgetComponent } from './budget.component';

describe('BudgetComponent', () => {
  let component: BudgetComponent;
  let fixture: ComponentFixture<BudgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BudgetComponent],
      providers: [
        MessageService,
        provideRouter([]),
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: '2' } } } },
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BudgetComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
