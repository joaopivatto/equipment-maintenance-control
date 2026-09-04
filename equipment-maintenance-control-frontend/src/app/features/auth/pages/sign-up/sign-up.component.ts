import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BaseProfile, ContentBoxComponent, ProfileType } from '../../../../shared';
import { SignUpService } from './service/sign-up.service';

@Component({
  imports: [ReactiveFormsModule, RouterLink, ContentBoxComponent],
  selector: 'app-sign-up',
  styleUrl: './sign-up.component.scss',
  templateUrl: './sign-up.component.html',
})
export class SignUpComponent {
  private readonly fb = inject(FormBuilder);
  private readonly signUpService = inject(SignUpService);

  readonly signUpForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    cpf: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    confirmEmail: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', Validators.required],
    address: this.fb.nonNullable.group({
      street: ['', Validators.required],
      number: ['', Validators.required],
      complement: [''],
      neighborhood: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      zipCode: ['', Validators.required],
    }),
  });

  constructor() {
    this.watchZipCode();
    this.watchCpf();
    this.watchEmailConfirmation();
  }

  async onSubmit(): Promise<void> {
    if (this.signUpForm.invalid) {
      this.signUpForm.markAllAsTouched();
      return;
    }

    const { name, email, cpf, phoneNumber, address } = this.signUpForm.getRawValue();

    const success = await this.signUpService.signUp({
      name,
      email,
      profileType: ProfileType.CUSTOMER,
      cpf,
      phoneNumber,
      address,
    });

    if (success) {
      alert('Cadastro realizado com sucesso!');
      this.signUpForm.reset();
      return;
    }
    alert('Ocorreu um erro ao realizar o cadastro. Por favor, tente novamente.');
    this.signUpForm.reset();
  }

  private watchZipCode() {
    this.signUpForm.get('address.zipCode')?.valueChanges.subscribe((zipCode) => {
      const digits = (zipCode ?? '').replace(/\D/g, '');
      if (digits.length === 8) {
        this.signUpService.getAddressByZipCode(digits).then((address) => {
          this.signUpForm.get('address')?.patchValue(
            {
              street: address.street,
              number: address.number,
              complement: address.complement,
              neighborhood: address.neighborhood,
              city: address.city,
              state: address.state,
              zipCode: address.zipCode,
            },
            { emitEvent: false },
          );
        });
      }
    });
  }

  private watchCpf() {
    this.signUpForm.get('cpf')?.valueChanges.subscribe((cpf) => {
      if (cpf && cpf.length === 11) {
        const isValid = this.signUpService.cpfIsValid(cpf);
        if (!isValid) {
          this.signUpForm.get('cpf')?.setErrors({ invalid: true });
        } else {
          this.signUpForm.get('cpf')?.setErrors(null);
        }
      }
    });
  }

  private watchEmailConfirmation() {
    this.signUpForm.get('email')?.valueChanges.subscribe(() => this.validateEmailConfirmation());
    this.signUpForm
      .get('confirmEmail')
      ?.valueChanges.subscribe(() => this.validateEmailConfirmation());
  }

  private validateEmailConfirmation() {
    const email = this.signUpForm.get('email')?.value;
    const confirmEmail = this.signUpForm.get('confirmEmail')?.value;

    if (email && confirmEmail) {
      const isValid = this.signUpService.emailIsValid(email, confirmEmail);
      this.signUpForm.get('confirmEmail')?.setErrors(isValid ? null : { mismatch: true });
    }
  }
}
