import { Routes } from '@angular/router';
import { authGuard, employeeGuard, guestGuard } from './core/auth/guards';

export const routes: Routes = [
  {
    path: '',
    canActivate: [guestGuard],
    loadChildren: () => import('./features/auth').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'requests',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout').then((m) => m.AuthenticatedLayoutComponent),
    loadChildren: () =>
      import('./features/maintenance-requests').then((m) => m.MAINTENANCE_REQUESTS_ROUTES),
  },
  {
    path: 'categories',
    canActivate: [employeeGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout').then((m) => m.AuthenticatedLayoutComponent),
    loadChildren: () =>
      import('./features/equipment-categories').then((m) => m.EQUIPMENT_CATEGORIES_ROUTES),
  },
  {
    path: 'employees',
    canActivate: [employeeGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout').then((m) => m.AuthenticatedLayoutComponent),
    loadChildren: () => import('./features/employees').then((m) => m.EMPLOYEES_ROUTES),
  },
  {
    path: 'reports',
    canActivate: [employeeGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout').then((m) => m.AuthenticatedLayoutComponent),
    loadChildren: () => import('./features/reports').then((m) => m.REPORTS_ROUTES),
  },
  // sempre deixar por último, para que seja a última rota a ser verificada
  {
    path: '**',
    title: 'Página não encontrada | Sistema de Gerenciamento',
    loadComponent: () => import('./core/pages/not-found').then((m) => m.NotFoundComponent),
  },
];
