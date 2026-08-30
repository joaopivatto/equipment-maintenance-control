import { Routes } from '@angular/router';

export const AUTH_ROUTES: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'customer-registration', loadComponent: () => import('./pages/customer-registration/customer-registration.component').then(m => m.CustomerRegistrationComponent) }
];
