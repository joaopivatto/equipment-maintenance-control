import { Routes } from '@angular/router';

export const AUTH_ROUTES: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    title: 'Entrar | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/login').then((m) => m.LoginComponent),
  },
  {
    path: 'sign-up',
    title: 'Criar conta | Sistema de Gerenciamento',
    loadComponent: () => import('./pages/sign-up').then((m) => m.SignUpComponent),
  },
];
