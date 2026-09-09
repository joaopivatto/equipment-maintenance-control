import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

@Component({
  imports: [RouterLink, ButtonModule, CardModule],
  selector: 'app-not-found',
  styleUrl: './not-found.component.scss',
  templateUrl: './not-found.component.html',
})
export class NotFoundComponent {}
