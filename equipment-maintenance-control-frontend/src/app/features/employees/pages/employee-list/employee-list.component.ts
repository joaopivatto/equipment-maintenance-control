import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';

@Component({
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-employee-list',
  styleUrl: './employee-list.component.scss',
  templateUrl: './employee-list.component.html',
})
export class EmployeeListComponent implements OnInit {
  private employeeService = inject(EmployeeService);
  private fb = inject(FormBuilder);

  // TODO: substituir por AuthService real quando o login/sessão estiver implementado
  private currentEmployeeId = 1;

  employees: Employee[] = [];
  editingId: number | null = null;

  form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    birthDate: ['', Validators.required],
    password: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
  });

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.employees = this.employeeService.listAll();
  }

  startEdit(employee: Employee): void {
    this.editingId = employee.id;
    this.form.get('password')?.clearValidators();
    this.form.get('password')?.updateValueAndValidity();
    this.form.patchValue({
      name: employee.name,
      email: employee.email,
      birthDate: employee.birthDate,
    });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.form.get('password')?.setValidators([Validators.required, Validators.pattern(/^\d{4}$/)]);
    this.form.get('password')?.updateValueAndValidity();
    this.form.reset();
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { name, email, birthDate, password } = this.form.value;

    if (this.editingId !== null) {
      this.employeeService.update(this.editingId, name!, email!, birthDate!);
    } else {
      this.employeeService.insert(name!, email!, birthDate!, password!);
    }

    this.cancelEdit();
    this.reload();
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
