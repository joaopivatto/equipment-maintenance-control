import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MaintenanceRequestApiClient, MockMaintenanceRequestApiClient } from '../../api';
import { EmployeeHomeComponent } from './employee-home.component';

describe('EmployeeHomeComponent', () => {
  let component: EmployeeHomeComponent;
  let fixture: ComponentFixture<EmployeeHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeHomeComponent],
      providers: [
        provideRouter([]),
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(EmployeeHomeComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
