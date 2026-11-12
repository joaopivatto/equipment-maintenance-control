import { Routes } from '@angular/router';
import { customerGuard, employeeGuard } from '../../core/auth/guards';

export const MAINTENANCE_REQUESTS_ROUTES: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  {
    path: 'list',
    title: 'Minhas solicitações | Sistema de Gerenciamento',
    loadComponent: () =>
      import('./pages/customer-request-list').then((m) => m.CustomerRequestListComponent),
  },
  {
    path: 'employee-home',
    canActivate: [employeeGuard],
    title: 'Solicitações de manutenção | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/employee-home').then((m) => m.EmployeeHomeComponent),
  },
  {
    path: 'new',
    canActivate: [customerGuard],
    title: 'Nova solicitação | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/new-request').then((m) => m.NewRequestComponent),
  },
  {
    path: ':id/details',
    title: 'Detalhes da solicitação | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/request-details').then((m) => m.RequestDetailsComponent),
  },
  {
    path: ':id/budget',
    title: 'Orçamento | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/budget').then((m) => m.BudgetComponent),
  },
  {
    path: ':id/maintenance',
    canActivate: [employeeGuard],
    title: 'Efetuar manutenção | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/maintenance').then((m) => m.MaintenanceComponent),
  },
  {
    path: ':id/payment',
    canActivate: [customerGuard],
    title: 'Pagamento | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/payment').then((m) => m.PaymentComponent),
  },
  {
    path: ':id/redirect',
    canActivate: [employeeGuard],
    title: 'Redirecionar solicitação | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/redirect').then((m) => m.RedirectComponent),
  },
];
