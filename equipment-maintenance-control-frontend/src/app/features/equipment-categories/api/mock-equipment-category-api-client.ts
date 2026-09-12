import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { EquipmentCategory } from '../models/equipment-category.model';
import { EquipmentCategoryMockFactory } from '../models/testing/equipment-category.mock';
import { EquipmentCategoryApiClient } from './equipment-category-api-client';

@Injectable()
export class MockEquipmentCategoryApiClient extends EquipmentCategoryApiClient {
  private readonly categoryFactory = new EquipmentCategoryMockFactory();

  private readonly categories: EquipmentCategory[] = Array.from({ length: 5 }, () =>
    this.categoryFactory.generate(),
  );

  private nextId = this.categories.length + 1;

  listAll(): Observable<EquipmentCategory[]> {
    // Só retorna as ativas (padrão de soft-delete exigido nos requisitos não-funcionais)
    return of(this.categories.filter((c) => c.active));
  }

  insert(name: string): Observable<{ category?: EquipmentCategory; error?: string }> {
    if (this.existsByName(name)) {
      return of({ error: `Categoria com o nome "${name}" já existe.` });
    }

    const category: EquipmentCategory = { id: this.nextId++, name, active: true };
    this.categories.push(category);

    return of({ category });
  }

  update(id: number, name: string): Observable<{ category?: EquipmentCategory; error?: string }> {
    const existingCategory = this.existsByName(name);
    if (existingCategory && existingCategory.id !== id) {
      return of({ error: `Categoria com o nome "${name}" já existe.` });
    }

    const category = this.categories.find((c) => c.id === id);
    if (!category) {
      return of({ error: 'Categoria não encontrada.' });
    }

    category.name = name;
    return of({ category });
  }

  deactivate(id: number): Observable<void> {
    const category = this.categories.find((c) => c.id === id);
    if (category) {
      category.active = false;
    }

    return of(undefined);
  }

  private existsByName(name: string): EquipmentCategory | undefined {
    return this.categories.find((c) => c.name === name && c.active);
  }
}
