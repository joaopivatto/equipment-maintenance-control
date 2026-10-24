import { Routes } from '@angular/router';

export const EMPLOYEES_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    title: 'Funcionários | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/employee-list/employee-list.component').then((m) => m.EmployeeListComponent),
  },
  {
    path: 'new',
    title: 'Novo funcionário | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/employee-form/employee-form.component').then((m) => m.EmployeeFormComponent),
  },
  {
    path: ':id/edit',
    title: 'Editar funcionário | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/employee-form/employee-form.component').then((m) => m.EmployeeFormComponent),
  },
];
