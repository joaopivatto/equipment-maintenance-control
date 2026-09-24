import { TestBed } from '@angular/core/testing';
import { ReportsPdfService } from './reports-pdf.service';

describe('ReportsPdfService', () => {
  let service: ReportsPdfService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportsPdfService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
