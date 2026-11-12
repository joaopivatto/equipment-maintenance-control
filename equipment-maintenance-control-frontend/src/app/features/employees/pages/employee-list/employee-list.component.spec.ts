import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthApiClient, MockAuthApiClient } from '../../../../core/api';
import { EmployeeApiClient, MockEmployeeApiClient } from '../../api';
import { EmployeeListComponent } from './employee-list.component';

describe('EmployeeListComponent', () => {
  let component: EmployeeListComponent;
  let fixture: ComponentFixture<EmployeeListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeListComponent],
      providers: [
        MessageService,
        provideRouter([]),
        { provide: EmployeeApiClient, useClass: MockEmployeeApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(EmployeeListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
