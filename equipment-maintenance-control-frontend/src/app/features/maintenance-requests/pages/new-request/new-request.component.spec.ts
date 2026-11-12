import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import {
  EquipmentCategoryApiClient,
  MockEquipmentCategoryApiClient,
} from '../../../equipment-categories/api';
import { MaintenanceRequestApiClient, MockMaintenanceRequestApiClient } from '../../api';
import { AuthApiClient, MockAuthApiClient } from '../../../../core/api';
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
        { provide: EquipmentCategoryApiClient, useClass: MockEquipmentCategoryApiClient },
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
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
