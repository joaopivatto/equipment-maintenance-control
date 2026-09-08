import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { SessionService } from '../../auth/session.service';
import { SidebarModule } from 'primeng/sidebar';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Wrench } from '@primeicons/angular/wrench';
import { PopoverModule } from 'primeng/popover';
import { SignOut } from '@primeicons/angular/sign-out';

@Component({
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    SidebarModule,
    AvatarModule,
    ButtonModule,
    CardModule,
    Wrench,
    PopoverModule,
    SignOut
  ],
  selector: 'app-authenticated-layout',
  styleUrl: './authenticated-layout.component.scss',
  templateUrl: './authenticated-layout.component.html',
})
export class AuthenticatedLayoutComponent {
  protected readonly sessionService = inject(SessionService);
  protected readonly router = inject(Router);

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
