import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'requests',
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
