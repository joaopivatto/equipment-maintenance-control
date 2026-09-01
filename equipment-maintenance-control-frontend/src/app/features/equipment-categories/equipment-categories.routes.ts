import { Routes } from '@angular/router';

export const EQUIPMENT_CATEGORIES_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    loadComponent: () =>
      import('./pages/category-list/category-list.component').then((m) => m.CategoryListComponent),
  },
];
