import { Component, computed, input } from '@angular/core';

import { CustomerInfo } from '../../models/maintenance-request.model';

@Component({
  selector: 'app-customer-details',
  standalone: true,
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.scss',
})
export class CustomerDetailsComponent {
  readonly customer = input<CustomerInfo | undefined>();
  readonly fallbackName = input<string | undefined>();

  readonly name = computed(() => this.customer()?.name ?? this.fallbackName() ?? '-');
  readonly cpf = computed(() => this.formatCpf(this.customer()?.cpf));
  readonly phone = computed(() => this.formatPhone(this.customer()?.phone));
  readonly address = computed(() => this.formatAddress(this.customer()));

  private formatCpf(value: string | undefined): string {
    const digits = (value ?? '').replace(/\D/g, '');
    if (digits.length !== 11) {
      return value || '-';
    }
    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`;
  }

  private formatPhone(value: string | undefined): string {
    const digits = (value ?? '').replace(/\D/g, '');
    if (digits.length === 11) {
      return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
    }
    if (digits.length === 10) {
      return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`;
    }
    return value || '-';
  }

  private formatAddress(customer: CustomerInfo | undefined): string {
    const address = customer?.address;
    if (!address) {
      return '-';
    }

    const zip = (address.zipCode ?? '').replace(/\D/g, '');
    const street = [address.street, address.number, address.complement]
      .filter((part) => part !== undefined && part !== null && part !== '')
      .join(', ');
    const cityState = [address.city, address.state].filter(Boolean).join('/');
    const parts = [
      street,
      address.neighborhood,
      cityState,
      zip.length === 8 ? `CEP ${zip.slice(0, 5)}-${zip.slice(5)}` : address.zipCode,
    ].filter(Boolean);

    return parts.length ? parts.join(' - ') : '-';
  }
}
