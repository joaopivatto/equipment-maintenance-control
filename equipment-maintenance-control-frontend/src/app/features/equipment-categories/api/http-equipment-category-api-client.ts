import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { API_BASE_URL, notifyAndStop, toErrorMessage } from '../../../core/api';
import { NotificationService } from '../../../core/notifications';
import { EquipmentCategory } from '../models';
import { EquipmentCategoryApiClient } from './equipment-category-api-client';
import { ApiEquipmentTypeDto } from './dto';
import { toEquipmentCategory } from './mappers';

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
