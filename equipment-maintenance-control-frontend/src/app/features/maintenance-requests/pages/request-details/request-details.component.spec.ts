import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { MaintenanceRequestApiClient, MockMaintenanceRequestApiClient } from '../../api';
import { AuthApiClient, MockAuthApiClient } from '../../../../core/api';
import { ProfileType } from '../../../../shared';
import { MaintenanceRequest, RequestStatus } from '../../models';
import { RequestDetailsComponent } from './request-details.component';

function storeSession(profileType: ProfileType): void {
  localStorage.setItem(
    'session-user',
    JSON.stringify({
      id: 1,
      name: 'Usuário de teste',
      email: 'teste@exemplo.com',
      profileType,
      token: 'test-token',
    }),
  );
}

function requestWithStatus(status: RequestStatus): MaintenanceRequest {
  return {
    id: 1,
    createdAt: new Date().toISOString(),
    equipmentDescription: 'Notebook',
    defectDescription: 'Não liga',
    status,
    history: [],
  };
}

describe('RequestDetailsComponent', () => {
  let component: RequestDetailsComponent;
  let fixture: ComponentFixture<RequestDetailsComponent>;

  async function createComponent(profileType: ProfileType): Promise<void> {
    storeSession(profileType);

    await TestBed.configureTestingModule({
      imports: [RequestDetailsComponent],
      providers: [
        MessageService,
        provideRouter([]),
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: '1' } } } },
        { provide: MaintenanceRequestApiClient, useClass: MockMaintenanceRequestApiClient },
        { provide: AuthApiClient, useClass: MockAuthApiClient },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RequestDetailsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  }

  function actionLabels(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll('.details-actions button')).map(
      (button) => (button as HTMLElement).textContent?.trim() ?? '',
    );
  }

  afterEach(() => localStorage.removeItem('session-user'));

  it('should create', async () => {
    await createComponent(ProfileType.CUSTOMER);

    expect(component).toBeTruthy();
  });

  describe('ações exclusivas do cliente', () => {
    const customerActions: [RequestStatus, string][] = [
      [RequestStatus.QUOTED, 'Ver orçamento'],
      [RequestStatus.REJECTED, 'Resgatar serviço'],
      [RequestStatus.REPAIRED, 'Pagar'],
    ];

    for (const [status, label] of customerActions) {
      it(`exibe "${label}" para o cliente quando a solicitação está ${status}`, async () => {
        await createComponent(ProfileType.CUSTOMER);

        component.request.set(requestWithStatus(status));
        await fixture.whenStable();

        expect(actionLabels()).toContain(label);
      });

      it(`não exibe "${label}" para o funcionário quando a solicitação está ${status}`, async () => {
        await createComponent(ProfileType.EMPLOYEE);

        component.request.set(requestWithStatus(status));
        await fixture.whenStable();

        expect(actionLabels()).toEqual([]);
        expect(component.hasActions()).toBe(false);
      });
    }
  });
});
