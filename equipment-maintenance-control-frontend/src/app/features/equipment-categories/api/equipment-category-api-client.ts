import { Observable } from 'rxjs';
import { EquipmentCategory } from '../models';

export abstract class EquipmentCategoryApiClient {
  abstract listAll(): Observable<EquipmentCategory[]>;

  abstract insert(name: string): Observable<{ category?: EquipmentCategory; error?: string }>;

  abstract update(
    id: number,
    name: string,
  ): Observable<{ category?: EquipmentCategory; error?: string }>;

  abstract deactivate(id: number): Observable<void>;
}
