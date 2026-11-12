import { EquipmentCategory } from '../../models';
import { ApiEquipmentTypeDto } from '../dto';

export function toEquipmentCategory(dto: ApiEquipmentTypeDto): EquipmentCategory {
  return { id: dto.id, name: dto.description, active: true };
}
