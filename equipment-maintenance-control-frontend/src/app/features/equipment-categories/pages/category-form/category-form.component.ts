import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EquipmentCategoryService } from '../../services/equipment-category.service';
import { EquipmentCategory } from '../../models/equipment-category.model';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './category-form.component.html',
  styleUrl: './category-form.component.css'
})
export class CategoryFormComponent implements OnInit {
  // Obtém a referência do formulário HTML para validações
  @ViewChild('formCategory') formCategory!: NgForm;

  private categoryService = inject(EquipmentCategoryService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  // Instancia um modelo de categoria vazio
  public category: EquipmentCategory = new EquipmentCategory();
  public isNew: boolean = true; // Flag para saber se é criação ou edição

  ngOnInit(): void {
    // Captura o parâmetro ":id" da URL (se existir)
    const id = this.route.snapshot.params['id'];

    if (id) {
      this.isNew = false;
      // Busca a categoria para edição de dentro da lista ativa do serviço
      const found = this.categoryService.listAll().find(c => c.id === +id);

      if (found) {
        // Clona o objeto para não alterar o serviço antes de clicar em "Salvar"
        this.category = { ...found };
      } else {
        alert('Categoria não encontrada!');
        this.router.navigate(['/categories']); // Volta para a listagem se der erro
      }
    }
  }

  save(): void {
    if (this.formCategory.form.valid) {
      if (this.isNew) {
        // Chama o método de inserção do serviço passando apenas o nome (conforme criado pelo seu grupo)
        this.categoryService.insert(this.category.name);
        alert('Categoria cadastrada com sucesso!');
      } else {
        // Chama o método de atualização do serviço passando ID e novo nome
        this.categoryService.update(this.category.id, this.category.name);
        alert('Categoria atualizada com sucesso!');
      }
      // Redireciona de volta para a tela de listagem
      this.router.navigate(['/categories']);
    }
  }
}
