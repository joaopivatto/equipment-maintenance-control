import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { EquipmentCategoryApiClient } from '../../api/equipment-category-api-client';
import { EquipmentCategory } from '../../models/equipment-category.model';

@Component({
  imports: [
    CommonModule,
    RouterLink,
    CardModule,
    ButtonModule,
    TableModule,
    SkeletonModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService],
  selector: 'app-category-list',
  styleUrl: './category-list.component.scss',
  templateUrl: './category-list.component.html',
})
export class CategoryListComponent implements OnInit {
  private categoryApiClient = inject(EquipmentCategoryApiClient);
  private location = inject(Location);
  private confirmationService = inject(ConfirmationService);

  isLoading = signal(true);
  protected readonly skeletonRows: EquipmentCategory[] = Array.from(
    { length: 5 },
    () => ({}) as EquipmentCategory,
  );

  categories: EquipmentCategory[] = [];

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.isLoading.set(true);
    this.categoryApiClient.listAll().subscribe((categories) => {
      this.categories = categories;
      this.isLoading.set(false);
    });
  }

  goBack(): void {
    this.location.back();
  }

  remove(category: EquipmentCategory): void {
    this.confirmationService.confirm({
      header: 'Remover categoria',
      message: `Deseja realmente remover a categoria "${category.name}"?`,
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Remover',
      rejectLabel: 'Cancelar',
      acceptButtonProps: { severity: 'danger' },
      rejectButtonProps: { severity: 'secondary', outlined: true },
      accept: () => {
        this.categoryApiClient.deactivate(category.id).subscribe(() => this.reload());
      },
    });
  }
}
