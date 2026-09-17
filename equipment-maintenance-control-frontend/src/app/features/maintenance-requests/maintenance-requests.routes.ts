import { Routes } from '@angular/router';
import { employeeGuard } from '../../core/auth/guards/profile.guard';

export const MAINTENANCE_REQUESTS_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    loadComponent: () =>
      import('./pages/customer-request-list/customer-request-list.component').then(
        (m) => m.CustomerRequestListComponent,
      ),
  },
  {
    path: 'open',
    canActivate: [employeeGuard],
    loadComponent: () =>
      import('./pages/employee-home/employee-home.component').then((m) => m.EmployeeHomeComponent),
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./pages/new-request/new-request.component').then((m) => m.NewRequestComponent),
  },
  {
    path: ':id/details',
    loadComponent: () =>
      import('./pages/request-details/request-details.component').then(
        (m) => m.RequestDetailsComponent,
      ),
  },
  {
    path: ':id/budget',
    loadComponent: () => import('./pages/budget/budget.component').then((m) => m.BudgetComponent),
  },
  {
    path: ':id/maintenance',
    loadComponent: () =>
      import('./pages/maintenance/maintenance.component').then((m) => m.MaintenanceComponent),
  },
  {
    path: ':id/redirect',
    loadComponent: () =>
      import('./pages/redirect/redirect.component').then((m) => m.RedirectComponent),
  },
];
