import { Injectable } from '@angular/core';
import { Service } from '@angular/core';
import { jsPDF } from 'jspdf';
import { autoTable } from 'jspdf-autotable';

import { CategoryRevenue, ReportsDashboard } from '../models';
import { DatePipe } from '@angular/common';

const PDF_MARGIN = 14;
const PDF_HEADER_COLOR: [number, number, number] = [13, 148, 136];

@Service()
export class ReportsPdfService {

  downloadRevenueReportsPdf(dashboard: ReportsDashboard): void {
    const document = new jsPDF({
      orientation: 'portrait',
      unit: 'mm',
      format: 'a4',
    });

    document.setProperties({
      title: `Relatórios de receitas (${this.formatDate(dashboard.revenueReport.startDate)} a ${this.formatDate(dashboard.revenueReport.endDate)})`,
      subject: 'Receitas por dia e por categoria',
      creator: 'Controle de Manutenção de Equipamentos',
    });

    this.addDailyRevenueSection(document, dashboard);
    this.addCategoryRevenueSection(document, dashboard.categoryRevenue);
    this.addPageNumbers(document);

    document.save(this.buildFileName(dashboard));
  }

  private addDailyRevenueSection(
    document: jsPDF,
    dashboard: ReportsDashboard,
  ): void {
    const report = dashboard.revenueReport;

    document.setFontSize(18);
    document.setTextColor(...PDF_HEADER_COLOR);
    document.text('Relatório de receitas por dia', PDF_MARGIN, 18);

    document.setFontSize(10);
    document.setTextColor(90);
    document.text(
      `Período: ${this.formatDate(report.startDate)} a ${this.formatDate(report.endDate)}`,
      PDF_MARGIN,
      26,
    );

    document.setFontSize(11);
    document.setTextColor(30);
    document.text(
      `Receita confirmada: ${this.formatCurrency(report.totalRevenue)}`,
      PDF_MARGIN,
      35,
    );
    document.text(
      `Pagamentos: ${report.paidRequestsCount}`,
      PDF_MARGIN,
      42,
    );
    document.text(
      `Ticket médio: ${this.formatCurrency(report.averageTicket)}`,
      90,
      42,
    );

    const dailyRevenueRows = report.dailyRevenue.length
      ? report.dailyRevenue.map((item) => [
          this.formatDate(item.date),
          item.paidRequestsCount.toString(),
          this.formatCurrency(item.totalRevenue),
        ])
      : [['Nenhuma receita encontrada no período', '', '']];

    autoTable(document, {
      startY: 50,
      head: [['Data', 'Pagamentos', 'Receita']],
      body: dailyRevenueRows,
      theme: 'striped',
      headStyles: {
        fillColor: PDF_HEADER_COLOR,
        textColor: 255,
      },
      margin: {
        left: PDF_MARGIN,
        right: PDF_MARGIN,
      },
    });
  }

  private addCategoryRevenueSection(
    document: jsPDF,
    categoryRevenue: CategoryRevenue[],
  ): void {
    document.addPage();

    document.setFontSize(18);
    document.setTextColor(...PDF_HEADER_COLOR);
    document.text('Relatório de receitas por categoria', PDF_MARGIN, 18);

    document.setFontSize(10);
    document.setTextColor(90);
    document.text(
      'Receita acumulada em todo o histórico da empresa',
      PDF_MARGIN,
      26,
    );

    const categoryRevenueRows = categoryRevenue.length
      ? categoryRevenue.map((item) => [
          item.categoryName,
          item.paidRequestsCount.toString(),
          this.formatCurrency(item.totalRevenue),
        ])
      : [['Nenhuma receita por categoria encontrada', '', '']];

    autoTable(document, {
      startY: 34,
      head: [['Categoria', 'Chamados pagos', 'Receita']],
      body: categoryRevenueRows,
      theme: 'striped',
      headStyles: {
        fillColor: PDF_HEADER_COLOR,
        textColor: 255,
      },
      margin: {
        left: PDF_MARGIN,
        right: PDF_MARGIN,
      },
    });
  }

  private addPageNumbers(document: jsPDF): void {
    const pageCount = document.getNumberOfPages();

    for (let pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
      document.setPage(pageNumber);
      document.setFontSize(9);
      document.setTextColor(120);

      document.text(
        `Página ${pageNumber} de ${pageCount}`,
        document.internal.pageSize.getWidth() - PDF_MARGIN,
        document.internal.pageSize.getHeight() - 8,
        { align: 'right' },
      );
    }
  }

  private buildFileName(dashboard: ReportsDashboard): string {
    const { startDate, endDate } = dashboard.revenueReport;
    return `relatorios-receitas-${this.formatDate(startDate)}-a-${this.formatDate(endDate)}.pdf`;
  }

  private formatDate(value: string): string {
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      timeZone: 'UTC',
    }).format(new Date(`${value.slice(0, 10)}T00:00:00Z`));
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  }
}
