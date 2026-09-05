import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Wrench } from '@primeicons/angular/wrench';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, SidebarModule, AvatarModule, ButtonModule, CardModule, Wrench],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('equipment-maintenance-control-frontend');

  isMobile = signal(false);
    constructor() {
        if (typeof window === 'undefined') return;
        const mql = window.matchMedia('(max-width: 1023px)');
        this.isMobile.set(mql.matches);
        mql.addEventListener('change', (e) => this.isMobile.set(e.matches));
    }

  protected readonly navItems = [
    { label: 'Solicitações', icon: 'pi pi-list', link: '/requests/list' },
    { label: 'Funcionários', icon: 'pi pi-users', link: '/employees/list' },
    { label: 'Categorias', icon: 'pi pi-tags', link: '/categories/list' },
  ];
}
