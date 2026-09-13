import { Injectable, effect, inject, signal } from '@angular/core';
import { SessionService } from '../../../core/auth/session.service';
import { EmployeeApiClient } from '../api/employee-api-client';
import { Employee } from '../models/employee.model';

// Correlaciona o usuário da sessão (SessionUser) com o funcionário cadastrado,
// já que SessionUser.id e Employee.id não são o mesmo identificador.
@Injectable({
  providedIn: 'root',
})
export class CurrentEmployeeService {
  private readonly sessionService = inject(SessionService);
  private readonly employeeApiClient = inject(EmployeeApiClient);

  private readonly currentEmployeeState = signal<Employee | undefined>(undefined);

  readonly currentEmployee = this.currentEmployeeState.asReadonly();

  constructor() {
    effect(() => {
      const email = this.sessionService.currentUser()?.email;
      if (!email) {
        this.currentEmployeeState.set(undefined);
        return;
      }

      this.employeeApiClient.findByEmail(email).subscribe((employee) => {
        this.currentEmployeeState.set(employee);
      });
    });
  }
}
