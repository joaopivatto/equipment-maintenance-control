import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { EquipmentCategoryService } from '../../services/equipment-category.service';
import { EquipmentCategory } from '../../models/equipment-category.model';

@Component({
  imports: [CommonModule, RouterLink, CardModule, ButtonModule, TableModule],
  selector: 'app-category-list',
  styleUrl: './category-list.component.scss',
  templateUrl: './category-list.component.html',
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(EquipmentCategoryService);
  private location = inject(Location);

  categories: EquipmentCategory[] = [];

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.categories = this.categoryService.listAll();
  }

  goBack(): void {
    this.location.back();
  }

  remove(category: EquipmentCategory): void {
    // Confirmação obrigatória antes de qualquer remoção (requisito não-funcional)
    if (confirm(`Deseja realmente remover a categoria "${category.name}"?`)) {
      this.categoryService.deactivate(category.id);
      this.reload();
    }
  }
}
