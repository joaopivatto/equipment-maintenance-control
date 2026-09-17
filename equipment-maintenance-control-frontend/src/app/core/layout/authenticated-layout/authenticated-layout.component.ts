import { Component, signal, inject, computed } from '@angular/core';
import { SlicePipe } from '@angular/common';
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
    SignOut,
    SlicePipe,
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

  private readonly allNavItems = [
    { label: 'Início', icon: 'pi pi-home', link: '/requests/employee-home', employeeOnly: true },
    { label: 'Solicitações', icon: 'pi pi-list', link: '/requests/list', employeeOnly: false },
    { label: 'Funcionários', icon: 'pi pi-users', link: '/employees/list', employeeOnly: true },
    { label: 'Categorias', icon: 'pi pi-tags', link: '/categories/list', employeeOnly: true },
    { label: 'Relatórios', icon: 'pi pi-chart-bar', link: '/reports', employeeOnly: true },
  ];

  // RF001/RF002 - Apenas funcionários podem ver os cadastros de funcionários e categorias
  protected readonly navItems = computed(() =>
    this.allNavItems.filter((item) => !item.employeeOnly || this.sessionService.isEmployee()),
  );

  logout(): void {
    this.sessionService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
}
