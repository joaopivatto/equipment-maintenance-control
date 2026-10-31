import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs';

import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';

import { MaintenanceRequestApiClient } from '../../api/maintenance-request-api-client';
import {
  MaintenanceRequest,
  RequestStatus,
  STATUS_CLASSES,
} from '../../models/maintenance-request.model';
import { NotificationService } from '../../../../core/notifications/notification.service';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.scss',
})
export class PaymentComponent implements OnInit {
  private readonly changeDetector = inject(ChangeDetectorRef);
  private maintenanceRequestApiClient = inject(MaintenanceRequestApiClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);

  readonly statusClasses = STATUS_CLASSES;

  request: MaintenanceRequest | undefined;
  paying = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);

    this.maintenanceRequestApiClient.findById(id).subscribe((request) => {
      this.request = request;
      this.changeDetector.markForCheck();

      if (!request) {
        this.notificationService.error('Erro', 'Solicitação não encontrada.');
        this.router.navigate(['/requests/list']);
        return;
      }

      if (request.status !== RequestStatus.REPAIRED) {
        this.notificationService.warning(
          'Aviso',
          'Esta solicitação não está aguardando pagamento.',
        );
        this.router.navigate(['/requests/list']);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  confirmPayment(): void {
    if (!this.request || this.paying) {
      return;
    }

    this.paying = true;

    this.maintenanceRequestApiClient
      .pay(this.request.id)
      .pipe(
        finalize(() => {
          this.paying = false;
          this.changeDetector.markForCheck();
        }),
      )
      .subscribe((updated) => {
        if (!updated) {
          this.notificationService.error('Erro', 'Não foi possível registrar o pagamento.');
          return;
        }

        this.notificationService.success(
          'Pagamento confirmado',
          'O pagamento foi registrado e a solicitação está paga.',
        );
        this.router.navigate(['/requests/list']);
      });
  }
}
