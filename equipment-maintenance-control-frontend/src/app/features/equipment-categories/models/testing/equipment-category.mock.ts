import { BaseMockFactory } from '../../../../shared/testing';
import { EquipmentCategory } from '../equipment-category.model';

export class EquipmentCategoryMockFactory extends BaseMockFactory<EquipmentCategory> {
  protected readonly length = 5;

  private readonly names = ['Notebook', 'Desktop', 'Impressora', 'Mouse', 'Teclado'];

  protected build(index: number): EquipmentCategory {
    return {
      id: index + 1,
      name: this.names[index],
      active: true,
    };
  }
}
