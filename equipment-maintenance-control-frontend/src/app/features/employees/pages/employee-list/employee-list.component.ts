import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ContentBoxComponent } from '../../../../shared';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';

@Component({
  imports: [CommonModule, RouterLink, ContentBoxComponent],
  selector: 'app-employee-list',
  styleUrl: './employee-list.component.scss',
  templateUrl: './employee-list.component.html',
})
export class EmployeeListComponent implements OnInit {
  private employeeService = inject(EmployeeService);

  // TODO: substituir por AuthService real quando o login/sessão estiver implementado
  private currentEmployeeId = 1;

  employees: Employee[] = [];

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.employees = this.employeeService.listAll();
  }

  remove(employee: Employee): void {
    const check = this.employeeService.canRemove(employee.id, this.currentEmployeeId);

    if (!check.allowed) {
      alert(check.reason);
      return;
    }

    // Confirmação obrigatória antes de qualquer remoção (requisito não-funcional)
    if (confirm(`Deseja realmente remover o funcionário "${employee.name}"?`)) {
      this.employeeService.deactivate(employee.id);
      this.reload();
    }
  }
}
