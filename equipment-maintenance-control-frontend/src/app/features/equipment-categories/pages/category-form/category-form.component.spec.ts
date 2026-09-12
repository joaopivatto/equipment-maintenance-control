import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { EquipmentCategoryApiClient } from '../../api/equipment-category-api-client';
import { MockEquipmentCategoryApiClient } from '../../api/mock-equipment-category-api-client';
import { CategoryFormComponent } from './category-form.component';

describe('CategoryFormComponent', () => {
  let component: CategoryFormComponent;
  let fixture: ComponentFixture<CategoryFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryFormComponent],
      providers: [
        MessageService,
        provideRouter([]),
        { provide: EquipmentCategoryApiClient, useClass: MockEquipmentCategoryApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoryFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
