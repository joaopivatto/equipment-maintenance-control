import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

import { NotificationService } from '../../../../core/notifications/notification.service';
import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import { MaintenanceRequest, RequestStatus } from '../../models/maintenance-request.model';
import { PaymentComponent } from './payment.component';

describe('PaymentComponent', () => {
  let fixture: ComponentFixture<PaymentComponent>;
  const navigate = vi.fn();
  const pay = vi.fn();
  const success = vi.fn();
  const warning = vi.fn();

  function buildRequest(status: RequestStatus): MaintenanceRequest {
    return {
      id: 7,
      createdAt: '2026-10-01T10:00:00',
      equipmentDescription: 'Notebook',
      defectDescription: 'Não liga',
      status,
      budget: { id: 1, value: 250, createdAt: '2026-10-02T10:00:00', employeeId: 1 },
      history: [],
    };
  }

  async function setup(status: RequestStatus) {
    navigate.mockReset();
    pay.mockReset();
    pay.mockReturnValue(of(buildRequest(RequestStatus.PAID)));

    await TestBed.configureTestingModule({
      imports: [PaymentComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: '7' } } } },
        { provide: Router, useValue: { navigate } },
        {
          provide: MaintenanceRequestApiClient,
          useValue: { findById: () => of(buildRequest(status)), pay },
        },
        { provide: NotificationService, useValue: { success, warning, error: vi.fn() } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PaymentComponent);
    fixture.detectChanges();
  }

  it('paga a solicitação ARRUMADA e volta para a lista', async () => {
    await setup(RequestStatus.REPAIRED);

    fixture.componentInstance.confirmPayment();

    expect(pay).toHaveBeenCalledWith(7);
    expect(success).toHaveBeenCalled();
    expect(navigate).toHaveBeenCalledWith(['/requests/list']);
  });

  it('não permite pagar quando a solicitação não está ARRUMADA', async () => {
    await setup(RequestStatus.OPEN);

    expect(warning).toHaveBeenCalled();
    expect(navigate).toHaveBeenCalledWith(['/requests/list']);
  });
});
