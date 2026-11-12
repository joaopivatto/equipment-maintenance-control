import { Routes } from '@angular/router';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

export const REPORTS_ROUTES: Routes = [
  {
    path: '',
    title: 'Relatórios | Sistema de Gerenciamento',
    providers: [provideCharts(withDefaultRegisterables())],
    loadComponent: () => import('./pages/reports').then((m) => m.ReportsComponent),
  },
];
