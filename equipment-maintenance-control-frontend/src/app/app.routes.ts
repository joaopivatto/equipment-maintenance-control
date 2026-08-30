import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES) },
  //ex para outras features { path: 'categories', loadChildren: () => import('./features/categories/categories.routes').then(m => m.CATEGORIES_ROUTES) },
];
