import { Injectable } from '@angular/core';
import { EquipmentCategory } from '../models/equipment-category.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentCategoryService {
  // Massa de teste - depois substituído pela API REST
  private categories: EquipmentCategory[] = [
    new EquipmentCategory(1, 'Notebook'),
    new EquipmentCategory(2, 'Desktop'),
    new EquipmentCategory(3, 'Impressora'),
    new EquipmentCategory(4, 'Mouse'),
    new EquipmentCategory(5, 'Teclado'),
  ];
  private nextId = 6;

  listAll(): EquipmentCategory[] {
    // Só retorna as ativas (padrão de soft-delete exigido nos requisitos não-funcionais)
    return this.categories.filter((c) => c.active);
  }

  insert(name: string): void {
    this.categories.push(new EquipmentCategory(this.nextId++, name));
  }

  update(id: number, name: string): void {
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
}
