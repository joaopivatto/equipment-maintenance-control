import { Routes } from '@angular/router';

import { ClienteHomeComponent } from './features/maintenance-requests/pages/cliente-home/cliente-home.component';

export const routes: Routes = [
  { path: '', redirectTo: 'cliente/home', pathMatch: 'full' },
  { path: 'cliente/home', component: ClienteHomeComponent }
];
