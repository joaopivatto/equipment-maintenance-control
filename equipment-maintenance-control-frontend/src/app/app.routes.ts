import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'cliente',
    loadChildren: () =>
      import('./features/maintenance-requests/maintenance-requests.routes').then(
        (m) => m.MAINTENANCE_REQUESTS_ROUTES,
      ),
  },
  {
    path: 'categories',
    loadChildren: () =>
      import('./features/equipment-categories/equipment-categories.routes').then(
        (m) => m.EQUIPMENT_CATEGORIES_ROUTES,
      ),
  },
  {
    path: 'employees',
    loadChildren: () =>
      import('./features/employees/employees.routes').then((m) => m.EMPLOYEES_ROUTES),
  },
  //ex para outras features { path: 'categories', loadChildren: () => import('./features/categories/categories.routes').then(m => m.CATEGORIES_ROUTES) },
];
