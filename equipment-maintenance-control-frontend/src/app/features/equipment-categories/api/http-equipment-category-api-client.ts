import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { API_BASE_URL } from '../../../core/api/api.config';
import { notifyAndStop, toErrorMessage } from '../../../core/api/api-error';
import { NotificationService } from '../../../core/notifications/notification.service';
import { EquipmentCategory } from '../models/equipment-category.model';
import { EquipmentCategoryApiClient } from './equipment-category-api-client';
import { ApiEquipmentTypeDto } from './dto/api-equipment-type.dto';
import { toEquipmentCategory } from './mappers/equipment-category.mapper';

@Injectable()
export class HttpEquipmentCategoryApiClient extends EquipmentCategoryApiClient {
  private readonly http = inject(HttpClient);
  private readonly notifications = inject(NotificationService);

  private readonly url = `${API_BASE_URL}/equipment-type`;

  listAll(): Observable<EquipmentCategory[]> {
    return this.http
      .get<ApiEquipmentTypeDto[]>(this.url)
      .pipe(map((categories) => categories.map(toEquipmentCategory)));
  }

  insert(name: string): Observable<{ category?: EquipmentCategory; error?: string }> {
    return this.http.post<ApiEquipmentTypeDto>(this.url, { description: name.trim() }).pipe(
      map((dto) => ({ category: toEquipmentCategory(dto) })),
      catchError((error: unknown) =>
        of({ error: toErrorMessage(error, 'Não foi possível cadastrar a categoria.') }),
      ),
    );
  }

  update(id: number, name: string): Observable<{ category?: EquipmentCategory; error?: string }> {
    return this.http
      .put<ApiEquipmentTypeDto>(`${this.url}/${id}`, { description: name.trim() })
      .pipe(
        map((dto) => ({ category: toEquipmentCategory(dto) })),
        catchError((error: unknown) =>
          of({ error: toErrorMessage(error, 'Não foi possível atualizar a categoria.') }),
        ),
      );
  }

  deactivate(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`).pipe(
      map(() => undefined),
      catchError(notifyAndStop<void>(this.notifications, 'Não foi possível remover a categoria.')),
    );
  }
}
