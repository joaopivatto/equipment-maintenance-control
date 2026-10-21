import { EquipmentCategory } from '../../models/equipment-category.model';
import { ApiEquipmentTypeDto } from '../dto/api-equipment-type.dto';

export function toEquipmentCategory(dto: ApiEquipmentTypeDto): EquipmentCategory {
  return { id: dto.id, name: dto.description, active: true };
}
