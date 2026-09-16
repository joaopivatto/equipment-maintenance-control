import { Routes } from '@angular/router';

export const MAINTENANCE_REQUESTS_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    loadComponent: () =>
      import('./pages/cliente-home/cliente-home.component').then((m) => m.ClienteHomeComponent),
  },
  {
    path: 'employee-home',
    loadComponent: () =>
      import('./pages/employee-home/employee-home.component').then((m) => m.EmployeeHomeComponent),
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./pages/new-request/new-request.component').then((m) => m.NewRequestComponent),
  },
  {
    path: ':id/budget',
    loadComponent: () => import('./pages/budget/budget.component').then((m) => m.BudgetComponent),
  },
];
