import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { EquipmentCategoryService } from '../../services/equipment-category.service';
import { EquipmentCategory } from '../../models/equipment-category.model';

@Component({
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-category-list',
  styleUrl: './category-list.component.scss',
  templateUrl: './category-list.component.html',
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(EquipmentCategoryService);
  private fb = inject(FormBuilder);

  categories: EquipmentCategory[] = [];
  editingId: number | null = null;

  form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.categories = this.categoryService.listAll();
  }

  startEdit(category: EquipmentCategory): void {
    this.editingId = category.id;
    this.form.setValue({ name: category.name });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.form.reset();
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const name = this.form.value.name!;

    if (this.editingId !== null) {
      this.categoryService.update(this.editingId, name);
    } else {
      this.categoryService.insert(name);
    }

    this.cancelEdit();
    this.reload();
  }

  remove(category: EquipmentCategory): void {
    // Confirmação obrigatória antes de qualquer remoção (requisito não-funcional)
    if (confirm(`Deseja realmente remover a categoria "${category.name}"?`)) {
      this.categoryService.deactivate(category.id);
      this.reload();
    }
  }
}
