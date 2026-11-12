import { Routes } from '@angular/router';

export const EQUIPMENT_CATEGORIES_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    title: 'Categorias de equipamentos | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/category-list').then((m) => m.CategoryListComponent),
  },
  {
    path: 'new',
    title: 'Nova categoria | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/category-form').then((m) => m.CategoryFormComponent),
  },
  {
    path: ':id/edit',
    title: 'Editar categoria | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/category-form').then((m) => m.CategoryFormComponent),
  },
];
