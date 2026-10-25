import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MessageService } from 'primeng/api';
import { NotificationService } from '../../../core/notifications/notification.service';
import { HttpEquipmentCategoryApiClient } from './http-equipment-category-api-client';

describe('HttpEquipmentCategoryApiClient', () => {
  let client: HttpEquipmentCategoryApiClient;
  let http: HttpTestingController;
  let notifyError: ReturnType<typeof vi.spyOn>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        HttpEquipmentCategoryApiClient,
        MessageService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    client = TestBed.inject(HttpEquipmentCategoryApiClient);
    http = TestBed.inject(HttpTestingController);
    notifyError = vi.spyOn(TestBed.inject(NotificationService), 'error');
  });

  afterEach(() => http.verify());

  it('lists categories and maps description to name', () => {
    let result: unknown;
    client.listAll().subscribe((categories) => (result = categories));

    http.expectOne('/api/equipment-type').flush([{ id: 1, description: 'Notebook' }]);

    expect(result).toEqual([{ id: 1, name: 'Notebook', active: true }]);
  });

  it('creates a category sending the trimmed description', () => {
    let result: unknown;
    client.insert('  Impressora  ').subscribe((response) => (result = response));

    const request = http.expectOne('/api/equipment-type');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({ description: 'Impressora' });
    request.flush({ id: 7, description: 'Impressora' });

    expect(result).toEqual({ category: { id: 7, name: 'Impressora', active: true } });
  });

  it('returns a translated error when the category cannot be updated', () => {
    let result: unknown;
    client.update(9, 'Mouse').subscribe((response) => (result = response));

    const request = http.expectOne('/api/equipment-type/9');
    expect(request.request.method).toBe('PUT');
    request.flush({ code: 'EQUIPMENT_TYPE_NOT_FOUND' }, { status: 404, statusText: 'Not Found' });

    expect(result).toEqual({ error: 'Categoria não encontrada.' });
  });

  it('removes a category with DELETE', () => {
    const complete = vi.fn();
    client.deactivate(3).subscribe({ complete });

    const request = http.expectOne('/api/equipment-type/3');
    expect(request.request.method).toBe('DELETE');
    request.flush(null, { status: 204, statusText: 'No Content' });

    expect(complete).toHaveBeenCalled();
  });

  it('notifies and emits nothing when the removal fails', () => {
    const next = vi.fn();
    client.deactivate(3).subscribe({ next });

    http
      .expectOne('/api/equipment-type/3')
      .flush({ code: 'EQUIPMENT_TYPE_ALREADY_INACTIVE' }, { status: 409, statusText: 'Conflict' });

    expect(next).not.toHaveBeenCalled();
    expect(notifyError).toHaveBeenCalledWith('Erro', 'Esta categoria já foi removida.');
  });
});
