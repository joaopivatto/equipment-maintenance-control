import { Injectable } from '@angular/core';
import { EquipmentCategory } from '../models/equipment-category.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentCategoryService {
  // Massa de teste - depois substituído pela API REST
  private categories: EquipmentCategory[] = [
    { id: 1, name: 'Notebook', active: true },
    { id: 2, name: 'Desktop', active: true },
    { id: 3, name: 'Impressora', active: true },
    { id: 4, name: 'Mouse', active: true },
    { id: 5, name: 'Teclado', active: true },
  ];
  private nextId = 6;

  listAll(): EquipmentCategory[] {
    // Só retorna as ativas (padrão de soft-delete exigido nos requisitos não-funcionais)
    return this.categories.filter((c) => c.active);
  }

  insert(name: string): void {
    if (this.existsByName(name)) {
      throw new Error(`Categoria com o nome "${name}" já existe.`);
    }
    this.categories.push({ id: this.nextId++, name, active: true });
  }

  update(id: number, name: string): void {
    const existingCategory = this.existsByName(name);
    if (existingCategory && existingCategory.id !== id) {
      throw new Error(`Categoria com o nome "${name}" já existe.`);
    }
    const category = this.categories.find((c) => c.id === id);
    if (category) {
      category.name = name;
    }
  }

  deactivate(id: number): void {
    const category = this.categories.find((c) => c.id === id);
    if (category) {
      category.active = false;
    }
  }

  existsByName(name: string): EquipmentCategory | undefined {
    return this.categories.find((c) => c.name === name && c.active);
  }
}
