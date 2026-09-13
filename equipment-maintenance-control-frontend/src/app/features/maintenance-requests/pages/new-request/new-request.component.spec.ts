import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MockMaintenanceRequestApiClient } from '../../api/mock-maintenance-request-api-client';
import { NewRequestComponent } from './new-request.component';

describe('NewRequestComponent', () => {
  let component: NewRequestComponent;
  let fixture: ComponentFixture<NewRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewRequestComponent],
      providers: [
        MessageService,
        provideRouter([]),
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(NewRequestComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
