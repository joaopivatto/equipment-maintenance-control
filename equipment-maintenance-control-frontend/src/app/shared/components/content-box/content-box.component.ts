import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-content-box',
  imports: [CommonModule],
  styleUrl: './content-box.component.scss',
  templateUrl: './content-box.component.html',
})
export class ContentBoxComponent {
  @Input() title?: string;
  @Input() subtitle?: string;
}
