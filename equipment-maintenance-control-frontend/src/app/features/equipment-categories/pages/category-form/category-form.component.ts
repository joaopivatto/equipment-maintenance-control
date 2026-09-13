import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { EquipmentCategoryApiClient } from '../../api/equipment-category-api-client';
import { EquipmentCategory } from '../../models/equipment-category.model';
import { NotificationService } from '../../../../core/notifications/notification.service';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    MessageModule,
  ],
  templateUrl: './category-form.component.html',
  styleUrl: './category-form.component.scss',
})
export class CategoryFormComponent implements OnInit {
  // Obtém a referência do formulário HTML para validações
  @ViewChild('categoryForm') categoryForm!: NgForm;

  private categoryApiClient = inject(EquipmentCategoryApiClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private notificationService = inject(NotificationService);

  // Instancia um modelo de categoria vazio
  category: EquipmentCategory = { id: 0, name: '', active: true };
  isEditing = false;

  ngOnInit(): void {
    // Captura o parâmetro ":id" da URL (se existir)
    const id = this.route.snapshot.params['id'];

    if (id) {
      this.isEditing = true;
      // Busca a categoria para edição de dentro da lista ativa do serviço
      this.categoryApiClient.listAll().subscribe((categories) => {
        const found = categories.find((c) => c.id === +id);

        if (found) {
          // Clona o objeto para não alterar o serviço antes de clicar em "Salvar"
          this.category = { ...found };
        } else {
          this.notificationService.error('Erro', 'Categoria não encontrada!');
          this.router.navigate(['/categories/list']); // Volta para a listagem se der erro
        }
      });
    }
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.categoryForm.form.valid) {
      const request$ = !this.isEditing
        ? this.categoryApiClient.insert(this.category.name)
        : this.categoryApiClient.update(this.category.id, this.category.name);

      request$.subscribe(({ error }) => {
        if (error) {
          this.notificationService.error('Erro', error);
          return;
        }

        this.notificationService.success(
          'Sucesso',
          this.isEditing
            ? 'Categoria atualizada com sucesso!'
            : 'Categoria cadastrada com sucesso!',
        );
        // Redireciona de volta para a tela de listagem
        this.router.navigate(['/categories/list']);
      });
    } else {
      this.notificationService.warning(
        'Aviso',
        'Por favor, corrija os erros no formulário antes de enviar.',
      );
    }
  }
}
