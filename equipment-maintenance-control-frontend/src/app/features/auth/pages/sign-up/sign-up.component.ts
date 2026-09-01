import { Component, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { BaseProfile, ProfileType } from '../../../../shared';

@Component({
  imports: [],
  selector: 'app-sign-up',
  styleUrl: './sign-up.component.scss',
  templateUrl: './sign-up.component.html',
})
export class SignUpComponent {
  private readonly fb = inject(FormBuilder);

  readonly signUpForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    cpf: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    confirmEmail: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', Validators.required],
    address: this.fb.group({
      street: ['', Validators.required],
      number: ['', Validators.required],
      complement: [''],
      neighborhood: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      zipCode: ['', Validators.required],
    }),
  });

  onSubmit(): void {
    if (this.signUpForm.invalid) {
      this.signUpForm.markAllAsTouched();
      return;
    }

    const { name, email, confirmEmail, cpf, phoneNumber, address } = this.signUpForm.getRawValue();
  }
}
