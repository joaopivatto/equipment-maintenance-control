import { Routes } from '@angular/router';

export const MAINTENANCE_REQUESTS_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    title: 'Minhas solicitações | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/customer-request-list/customer-request-list.component').then(
        (m) => m.CustomerRequestListComponent,
      ),
  },
  {
    path: 'employee-home',
    title: 'Solicitações de manutenção | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/employee-home/employee-home.component').then((m) => m.EmployeeHomeComponent),
  },
  {
    path: 'new',
    title: 'Nova solicitação | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/new-request/new-request.component').then((m) => m.NewRequestComponent),
  },
  {
    path: ':id/details',
    title: 'Detalhes da solicitação | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/request-details/request-details.component').then(
        (m) => m.RequestDetailsComponent,
      ),
  },
  {
    path: ':id/budget',
    title: 'Orçamento | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/budget/budget.component').then((m) => m.BudgetComponent),
  },
  {
    path: ':id/maintenance',
    title: 'Efetuar manutenção | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/maintenance/maintenance.component').then((m) => m.MaintenanceComponent),
  },
  {
    path: ':id/payment',
    title: 'Pagamento | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/payment/payment.component').then((m) => m.PaymentComponent),
  },
  {
    path: ':id/redirect',
    title: 'Redirecionar solicitação | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/redirect/redirect.component').then((m) => m.RedirectComponent),
  },
];
