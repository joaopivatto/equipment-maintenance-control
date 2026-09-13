import { Routes } from '@angular/router';
import { authGuard } from './core/auth/guards/auth.guard';
import { employeeGuard } from './core/auth/guards/profile.guard';
import { guestGuard } from './core/auth/guards/guest.guard';

export const routes: Routes = [
  {
    path: '',
    canActivate: [guestGuard],
    loadChildren: () => import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'requests',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout/authenticated-layout.component').then(
        (m) => m.AuthenticatedLayoutComponent,
      ),
    loadChildren: () => import('./features/maintenance-requests/maintenance-requests.routes').then(
          (m) => m.MAINTENANCE_REQUESTS_ROUTES,
        ),
  },
  {
    path: 'categories',
    canActivate: [employeeGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout/authenticated-layout.component').then(
        (m) => m.AuthenticatedLayoutComponent,
      ),
    loadChildren: () =>
      import('./features/equipment-categories/equipment-categories.routes').then(
        (m) => m.EQUIPMENT_CATEGORIES_ROUTES,
      ),
  },
  {
    path: 'employees',
    canActivate: [employeeGuard],
    loadComponent: () =>
      import('./core/layout/authenticated-layout/authenticated-layout.component').then(
        (m) => m.AuthenticatedLayoutComponent,
      ),
    loadChildren: () =>
      import('./features/employees/employees.routes').then((m) => m.EMPLOYEES_ROUTES),
  },
  // sempre deixar por último, para que seja a última rota a ser verificada
  {
    path: '**',
    loadComponent: () =>
      import('./core/pages/not-found/not-found.component').then((m) => m.NotFoundComponent),
  },
];
