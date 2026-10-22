import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { API_BASE_URL } from '../../../core/api/api.config';
import { notifyAndStop, toErrorMessage } from '../../../core/api/api-error';
import { NotificationService } from '../../../core/notifications/notification.service';
import { Employee } from '../models/employee.model';
import { EmployeeApiClient } from './employee-api-client';
import { ApiEmployeeDto } from './dto/api-employee.dto';
import { toEmployee } from './mappers/employee.mapper';

@Injectable()
export class HttpEmployeeApiClient extends EmployeeApiClient {
  private readonly http = inject(HttpClient);
  private readonly notifications = inject(NotificationService);

  private readonly url = `${API_BASE_URL}/employees`;

  listAll(): Observable<Employee[]> {
    return this.http
      .get<ApiEmployeeDto[]>(this.url)
      .pipe(map((employees) => employees.map(toEmployee)));
  }

  findById(id: number): Observable<Employee | undefined> {
    return this.http.get<ApiEmployeeDto>(`${this.url}/${id}`).pipe(
      map(toEmployee),
      catchError(() => of(undefined)),
    );
  }

  findByEmail(email: string): Observable<Employee | undefined> {
    const wanted = email.trim().toLowerCase();

    return this.listAll().pipe(
      map((employees) => employees.find((employee) => employee.email.toLowerCase() === wanted)),
      catchError(() => of(undefined)),
    );
  }

  insert(name: string, email: string, birthDate: string, password: string): Observable<Employee> {
    return this.http
      .post<ApiEmployeeDto>(this.url, {
        name: name.trim(),
        email: email.trim(),
        birthDate,
        password,
      })
      .pipe(
        map(toEmployee),
        catchError(
          notifyAndStop<Employee>(this.notifications, 'Não foi possível cadastrar o funcionário.'),
        ),
      );
  }

  update(
    id: number,
    name: string,
    email: string,
    birthDate: string,
  ): Observable<Employee | undefined> {
    return this.http
      .put<ApiEmployeeDto>(`${this.url}/${id}`, {
        name: name.trim(),
        email: email.trim(),
        birthDate,
      })
      .pipe(
        map(toEmployee),
        catchError(
          notifyAndStop<Employee>(this.notifications, 'Não foi possível atualizar o funcionário.'),
        ),
      );
  }

  deactivate(id: number, currentEmployeeId: number): Observable<{ error?: string }> {
    const params = new HttpParams().set('currentEmployeeId', currentEmployeeId);

    return this.http.delete<void>(`${this.url}/${id}`, { params }).pipe(
      map(() => ({}) as { error?: string }),
      catchError((error: unknown) =>
        of({ error: toErrorMessage(error, 'Não foi possível remover o funcionário.') }),
      ),
    );
  }
}
