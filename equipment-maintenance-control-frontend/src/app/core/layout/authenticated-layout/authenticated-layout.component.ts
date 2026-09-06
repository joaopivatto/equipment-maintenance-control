import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Wrench } from '@primeicons/angular/wrench';

@Component({
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    SidebarModule,
    AvatarModule,
    ButtonModule,
    CardModule,
    Wrench
  ],
  selector: 'app-authenticated-layout',
  styleUrl: './authenticated-layout.component.scss',
  templateUrl: './authenticated-layout.component.html',
})
export class AuthenticatedLayoutComponent {

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
