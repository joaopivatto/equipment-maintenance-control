import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('equipment-maintenance-control-frontend');

  protected readonly navItems = [
    { label: 'Solicitações', icon: 'pi pi-list', link: '/requests/list' },
    { label: 'Funcionários', icon: 'pi pi-users', link: '/employees/list' },
    { label: 'Categorias', icon: 'pi pi-tags', link: '/categories/list' },
  ];
}
