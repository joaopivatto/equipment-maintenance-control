import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { EmployeeService } from '../../services/employee.service';
import { DatePickerModule } from 'primeng/datepicker';
import { DateService } from '../../../../shared/services/date.service';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    CardModule,
    InputTextModule,
    ButtonModule,
    MessageModule,
    DatePickerModule
  ],
  selector: 'app-employee-form',
  styleUrl: './employee-form.component.scss',
  templateUrl: './employee-form.component.html',
})
export class EmployeeFormComponent implements OnInit {
  private employeeService = inject(EmployeeService);
  private dateService = inject(DateService);
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);

  editingId: number | null = null;

  form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    birthDate: this.fb.control<Date | null>(null, Validators.required),
    password: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
  });

  get isEditing(): boolean {
    return this.editingId !== null;
  }

  goBack(): void {
    this.location.back();
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
    // Na edição a senha não é alterada por aqui
    this.form.get('password')?.clearValidators();
    this.form.get('password')?.updateValueAndValidity();
    this.form.patchValue({
      name: employee.name,
      email: employee.email,
      birthDate: this.dateService.fromIsoDate(employee.birthDate),
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { name, email, birthDate, password } = this.form.value;

    const birthDateIso = this.dateService.toIsoDate(birthDate!);

    if (this.editingId !== null) {
      this.employeeService.update(this.editingId, name!, email!, birthDateIso);
    } else {
      this.employeeService.insert(name!, email!, birthDateIso, password!);
    }

    this.router.navigate(['/employees/list']);
  }
}
