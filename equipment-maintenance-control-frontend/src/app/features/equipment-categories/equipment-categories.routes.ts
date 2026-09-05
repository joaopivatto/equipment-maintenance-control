import { Routes } from '@angular/router';

export const EQUIPMENT_CATEGORIES_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    loadComponent: () =>
      import('./pages/category-list/category-list.component').then((m) => m.CategoryListComponent),
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./pages/category-form/category-form.component').then((m) => m.CategoryFormComponent),
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./pages/category-form/category-form.component').then((m) => m.CategoryFormComponent),
  },
];
