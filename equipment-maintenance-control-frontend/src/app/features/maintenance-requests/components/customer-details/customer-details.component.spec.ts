import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerDetailsComponent } from './customer-details.component';

describe('CustomerDetailsComponent', () => {
  let fixture: ComponentFixture<CustomerDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerDetailsComponent],
    }).compileComponents();
    fixture = TestBed.createComponent(CustomerDetailsComponent);
  });

  it('formata CPF, telefone e endereço do cliente', () => {
    fixture.componentRef.setInput('customer', {
      name: 'João',
      email: 'joao@email.com',
      cpf: '52998224725',
      phone: '41999990001',
      address: {
        zipCode: '80010000',
        street: 'Rua XV de Novembro',
        number: 100,
        complement: 'Apto 11',
        neighborhood: 'Centro',
        city: 'Curitiba',
        state: 'PR',
      },
    });
    fixture.detectChanges();

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('João');
    expect(text).toContain('529.982.247-25');
    expect(text).toContain('(41) 99999-0001');
    expect(text).toContain(
      'Rua XV de Novembro, 100, Apto 11 - Centro - Curitiba/PR - CEP 80010-000',
    );
  });

  it('usa o nome alternativo e traços quando não há dados do cliente', () => {
    fixture.componentRef.setInput('fallbackName', 'Maria');
    fixture.detectChanges();

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('Maria');
    expect(text).toContain('-');
  });
});
