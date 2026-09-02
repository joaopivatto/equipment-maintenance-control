import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitacaoService } from '../../services/solicitacao.service';

@Component({
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-new-request',
  styleUrl: './new-request.component.scss',
  templateUrl: './new-request.component.html',
})
export class NewRequestComponent {
  private fb = inject(FormBuilder);
  private solicitacaoService = inject(SolicitacaoService);
  private router = inject(Router);

  // Categorias mockadas por enquanto (RF017 ainda não implementado no backend)
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
    this.router.navigate(['/cliente/home']);
  }

  cancel(): void {
    this.router.navigate(['/cliente/home']);
  }
}
