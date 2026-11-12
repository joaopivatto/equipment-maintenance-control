import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { EquipmentCategoryApiClient, MockEquipmentCategoryApiClient } from '../../api';
import { CategoryListComponent } from './category-list.component';

describe('CategoryListComponent', () => {
  let component: CategoryListComponent;
  let fixture: ComponentFixture<CategoryListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryListComponent],
      providers: [
        provideRouter([]),
        { provide: EquipmentCategoryApiClient, useClass: MockEquipmentCategoryApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoryListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
