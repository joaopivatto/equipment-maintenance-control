import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClienteHomeComponent } from './cliente-home.component';

describe('ClienteHome', () => {
  let component: ClienteHomeComponent;
  let fixture: ComponentFixture<ClienteHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteHomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClienteHomeComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
