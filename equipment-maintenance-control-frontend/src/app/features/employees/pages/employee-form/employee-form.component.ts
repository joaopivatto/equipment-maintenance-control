import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ContentBoxComponent } from '../../../../shared';
import { EmployeeService } from '../../services/employee.service';

@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink, ContentBoxComponent],
  selector: 'app-employee-form',
  styleUrl: './employee-form.component.scss',
  templateUrl: './employee-form.component.html',
})
export class EmployeeFormComponent implements OnInit {
  private employeeService = inject(EmployeeService);
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  editingId: number | null = null;

  form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    birthDate: ['', Validators.required],
    password: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
  });

  get isEditing(): boolean {
    return this.editingId !== null;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      return;
    }

    const employee = this.employeeService.findById(Number(idParam));
    if (!employee) {
      this.router.navigate(['/employees/list']);
      return;
    }

    this.editingId = employee.id;
    // Na edição a senha não é alterada por aqui (RF018)
    this.form.get('password')?.clearValidators();
    this.form.get('password')?.updateValueAndValidity();
    this.form.patchValue({
      name: employee.name,
      email: employee.email,
      birthDate: employee.birthDate,
    });
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

    this.router.navigate(['/employees/list']);
  }
}
