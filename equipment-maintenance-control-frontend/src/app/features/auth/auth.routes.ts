import { Routes } from '@angular/router';

export const AUTH_ROUTES: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'sign-up', loadComponent: () => import('./pages/sign-up/sign-up.component').then(m => m.SignUpComponent) }
];
