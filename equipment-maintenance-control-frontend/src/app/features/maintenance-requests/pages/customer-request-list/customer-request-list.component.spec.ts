import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';

import { CustomerRequestListComponent } from './customer-request-list.component';

describe('CustomerRequestListComponent', () => {
  let component: CustomerRequestListComponent;
  let fixture: ComponentFixture<CustomerRequestListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerRequestListComponent],
      providers: [provideRouter([]), MessageService],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerRequestListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
