import { Routes } from '@angular/router';

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
    path: 'new',
    loadComponent: () =>
      import('./pages/new-request/new-request.component').then((m) => m.NewRequestComponent),
  },
  {
    path: ':id/budget',
    loadComponent: () => import('./pages/budget/budget.component').then((m) => m.BudgetComponent),
  },
];
