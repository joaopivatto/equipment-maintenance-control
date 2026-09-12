import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { EquipmentCategoryApiClient } from '../../../equipment-categories/api/equipment-category-api-client';
import { MockEquipmentCategoryApiClient } from '../../../equipment-categories/api/mock-equipment-category-api-client';
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
