import { Injectable, effect, inject, signal } from '@angular/core';
import { SessionService } from '../../../core/auth';
import { EmployeeApiClient } from '../api';
import { Employee } from '../models';

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
      if (!email || !this.sessionService.isEmployee()) {
        this.currentEmployeeState.set(undefined);
        return;
      }

      this.employeeApiClient.findByEmail(email).subscribe((employee) => {
        this.currentEmployeeState.set(employee);
      });
    });
  }
}
