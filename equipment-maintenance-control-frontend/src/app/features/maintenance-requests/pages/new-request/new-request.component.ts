import { Component, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { MaintenanceRequestService } from '../../services/maintenance-request.service';
import { NotificationService } from '../../../../core/notifications/notification.service';
import { EquipmentCategoryService } from '../../../equipment-categories/services/equipment-category.service';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    SelectModule,
    TextareaModule,
    ButtonModule,
    MessageModule,
  ],
  selector: 'app-new-request',
  styleUrl: './new-request.component.scss',
  templateUrl: './new-request.component.html',
})
export class NewRequestComponent {
  private formBuilder = inject(FormBuilder);
  private maintenanceRequestService = inject(MaintenanceRequestService);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);

  private equipmentCategoryService = inject(EquipmentCategoryService);

  // Categorias mockadas por enquanto (backend ainda não implementado)
  equipmentCategories = this.equipmentCategoryService.listAll();

  form = this.formBuilder.group({
    equipmentDescription: ['', [Validators.required, Validators.maxLength(100)]],
    equipmentCategoryId: this.formBuilder.control<number | null>(null, Validators.required),
    defectDescription: ['', [Validators.required, Validators.maxLength(500)]],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.notificationService.warning(
        'Aviso',
        'Por favor, corrija os erros no formulário antes de enviar.',
      );
      return;
    }

    // TODO: substituir por chamada real à API REST quando o backend estiver pronto
    console.log('Nova solicitação:', this.form.value);
    this.notificationService.success('Sucesso', 'Solicitação registrada com sucesso!');
    this.router.navigate(['/requests/list']);
  }

  cancel(): void {
    this.router.navigate(['/requests/list']);
  }

  goBack(): void {
    this.location.back();
  }
}
