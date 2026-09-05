import { Component, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { SolicitacaoService } from '../../services/solicitacao.service';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    SelectModule,
    TextareaModule,
    ButtonModule,
    MessageModule,
  ],
  selector: 'app-new-request',
  styleUrl: './new-request.component.scss',
  templateUrl: './new-request.component.html',
})
export class NewRequestComponent {
  private fb = inject(FormBuilder);
  private solicitacaoService = inject(SolicitacaoService);
  private router = inject(Router);
  private location = inject(Location);

  // Categorias mockadas por enquanto (backend ainda não implementado)
  categorias = ['Notebook', 'Desktop', 'Impressora', 'Mouse', 'Teclado'];

  form = this.fb.group({
    descricaoEquipamento: ['', [Validators.required, Validators.maxLength(100)]],
    categoria: ['', Validators.required],
    descricaoDefeito: ['', [Validators.required, Validators.maxLength(500)]],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // TODO: substituir por chamada real à API REST quando o backend estiver pronto
    console.log('Nova solicitação:', this.form.value);
    alert('Solicitação registrada com sucesso!');
    this.router.navigate(['/requests/list']);
  }

  cancel(): void {
    this.router.navigate(['/requests/list']);
  }

  goBack(): void {
    this.location.back();
  }
}
