import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputOtpModule } from 'primeng/inputotp';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SessionService } from '../../../../core/auth/session.service';
import { NotificationService } from '../../../../core/notifications/notification.service';

@Component({
  imports: [
    ReactiveFormsModule,
    RouterLink,
    CardModule,
    InputTextModule,
    InputOtpModule,
    ButtonModule,
    MessageModule,
  ],
  selector: 'app-login',
  styleUrl: './login.component.scss',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private readonly sessionService = inject(SessionService);
  private readonly notificationService = inject(NotificationService);
  private readonly formBuilder = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly invalidCredentials = signal(false);

  readonly form = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
  });

  constructor() {
    this.form.valueChanges.subscribe(() => this.invalidCredentials.set(false));
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

    const { email, password } = this.form.value;

    this.sessionService.login(email!, password!).subscribe((profileType) => {
      if (!profileType) {
        this.invalidCredentials.set(true);
        return;
      }

      // Identifica se o perfil retornado é de funcionário
      const profileStr = String(profileType).toUpperCase();
      const isEmployee = profileStr === 'EMPLOYEE' || profileStr === 'FUNC';

      if (isEmployee) {
        // Redireciona o funcionário para a tela de solicitações abertas (RF011)
        this.router.navigate(['/requests/employee-home']);
      } else {
        // Redireciona o cliente para a sua página inicial (RF003)
        this.router.navigate(['/requests/list']);
      }
    });
  }
}
