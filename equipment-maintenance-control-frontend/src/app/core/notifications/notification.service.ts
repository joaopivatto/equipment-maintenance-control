import { Service, inject } from '@angular/core';
import { MessageService } from 'primeng/api';

@Service()
export class NotificationService {
  private messageService = inject(MessageService);

  success(title: string = 'Sucesso', message: string = 'Operação realizada com sucesso.'): void {
    this.messageService.add({ severity: 'success', summary: title, detail: message });
  }

  error(title: string = 'Erro', message: string = 'Ocorreu um erro ao realizar a operação.'): void {
    this.messageService.add({ severity: 'error', summary: title, detail: message });
  }

  warning(title: string = 'Aviso', message: string = 'Atenção! Verifique as informações.'): void {
    this.messageService.add({ severity: 'warn', summary: title, detail: message });
  }
}
