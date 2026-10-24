import { Routes } from '@angular/router';

export const EQUIPMENT_CATEGORIES_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    title: 'Categorias de equipamentos | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/category-list/category-list.component').then((m) => m.CategoryListComponent),
  },
  {
    path: 'new',
    title: 'Nova categoria | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/category-form/category-form.component').then((m) => m.CategoryFormComponent),
  },
  {
    path: ':id/edit',
    title: 'Editar categoria | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/category-form/category-form.component').then((m) => m.CategoryFormComponent),
  },
];
