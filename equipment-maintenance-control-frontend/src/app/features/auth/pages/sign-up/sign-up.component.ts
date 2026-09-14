import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { debounceTime, distinctUntilChanged, filter } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputMaskModule } from 'primeng/inputmask';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { SignUpService } from '../../services/sign-up.service';
import { AuthApiClient } from '../../../../core/api/auth-api-client';

const UF_OPTIONS = [
  { label: 'Acre', value: 'AC' },
  { label: 'Alagoas', value: 'AL' },
  { label: 'Amapá', value: 'AP' },
  { label: 'Amazonas', value: 'AM' },
  { label: 'Bahia', value: 'BA' },
  { label: 'Ceará', value: 'CE' },
  { label: 'Distrito Federal', value: 'DF' },
  { label: 'Espírito Santo', value: 'ES' },
  { label: 'Goiás', value: 'GO' },
  { label: 'Maranhão', value: 'MA' },
  { label: 'Mato Grosso', value: 'MT' },
  { label: 'Mato Grosso do Sul', value: 'MS' },
  { label: 'Minas Gerais', value: 'MG' },
  { label: 'Pará', value: 'PA' },
  { label: 'Paraíba', value: 'PB' },
  { label: 'Paraná', value: 'PR' },
  { label: 'Pernambuco', value: 'PE' },
  { label: 'Piauí', value: 'PI' },
  { label: 'Rio de Janeiro', value: 'RJ' },
  { label: 'Rio Grande do Norte', value: 'RN' },
  { label: 'Rio Grande do Sul', value: 'RS' },
  { label: 'Rondônia', value: 'RO' },
  { label: 'Roraima', value: 'RR' },
  { label: 'Santa Catarina', value: 'SC' },
  { label: 'São Paulo', value: 'SP' },
  { label: 'Sergipe', value: 'SE' },
  { label: 'Tocantins', value: 'TO' },
];

@Component({
  imports: [
    ReactiveFormsModule,
    RouterLink,
    CardModule,
    InputTextModule,
    InputMaskModule,
    SelectModule,
    ButtonModule,
    MessageModule,
  ],
  selector: 'app-sign-up',
  styleUrl: './sign-up.component.scss',
  templateUrl: './sign-up.component.html',
})
export class SignUpComponent {
  private readonly apiClient = inject(AuthApiClient);
  private readonly notificationService = inject(NotificationService);
  private readonly signUpService = inject(SignUpService);
  private readonly formBuilder = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly ufOptions = UF_OPTIONS;

  readonly form = this.formBuilder.group(
    {
      name: ['', [Validators.required, Validators.maxLength(100)]],
      cpf: ['', [Validators.required, this.signUpService.cpfValidator()]],
      email: ['', [Validators.required, Validators.email]],
      emailConfirmation: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{10,11}$/)]],
      address: this.formBuilder.group({
        zipCode: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
        street: ['', [Validators.required, Validators.maxLength(150)]],
        number: this.formBuilder.control<number | null>(null, [
          Validators.required,
          Validators.min(1),
        ]),
        complement: [''],
        neighborhood: ['', [Validators.required, Validators.maxLength(100)]],
        city: ['', [Validators.required, Validators.maxLength(100)]],
        state: ['', [Validators.required]],
      }),
    },
    { validators: this.signUpService.emailValidator('email', 'emailConfirmation') },
  );

  constructor() {
    this.form
      .get('address.zipCode')
      ?.valueChanges.pipe(
        debounceTime(400),
        distinctUntilChanged(),
        filter((value): value is string => !!value && value.length === 8),
      )
      .subscribe((zipCode) => this.lookupAddress(zipCode));
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.notificationService.warning(
        'Aviso',
        'Por favor, corrija os erros no formulário antes de enviar.',
      );
      return;
    }

    const { name, cpf, email, phoneNumber, address } = this.form.getRawValue();

    this.apiClient
      .signUp({
        name: name!,
        email: email!,
        cpf: cpf!,
        phoneNumber: phoneNumber!,
        address: {
          street: address.street!,
          complement: address.complement ?? '',
          neighborhood: address.neighborhood!,
          number: address.number!,
          city: address.city!,
          state: address.state!,
          zipCode: address.zipCode!,
        },
      })
      .subscribe(({ error }) => {
        if (error) {
          this.notificationService.error('Erro', error);
          return;
        }

        this.notificationService.success(
          'Sucesso',
          'Cadastro realizado com sucesso! Sua senha de acesso foi enviada para o e-mail informado.',
        );
        this.router.navigate(['/login']);
      });
  }

  private lookupAddress(zipCode: string): void {
    this.apiClient.findAddressByZipCode(zipCode).subscribe((address) => {
      if (!address) {
        this.notificationService.warning(
          'Aviso',
          'Não foi possível localizar o CEP informado. Preencha o endereço manualmente.',
        );
        return;
      }

      this.form.controls.address.patchValue({
        street: address.street,
        neighborhood: address.neighborhood,
        city: address.city,
        state: address.state,
      });
    });
  }
}
