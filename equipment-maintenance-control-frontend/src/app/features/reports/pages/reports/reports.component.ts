import { CurrencyPipe, DatePipe, DecimalPipe, Location } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { NotificationService } from '../../../../core/notifications/notification.service';
import { ReportsDashboard } from '../../models';
import { ReportsService } from '../../services/reports.service';

type PeriodPreset = 'last-month' | 'last-three-months' | 'last-six-months' | 'last-year' | 'custom';

@Component({
  imports: [
    FormsModule,
    ButtonModule,
    CardModule,
    BaseChartDirective,
    DatePickerModule,
    SelectModule,
    SkeletonModule,
    TableModule,
    CurrencyPipe,
    DatePipe,
    DecimalPipe,
  ],
  selector: 'app-reports',
  styleUrl: './reports.component.scss',
  templateUrl: './reports.component.html',
})
export class ReportsComponent implements OnInit {
  private readonly location = inject(Location);
  private readonly reportsService = inject(ReportsService);
  private readonly notificationService = inject(NotificationService);

  protected readonly isLoading = signal(true);
  protected readonly dashboard = signal<ReportsDashboard | null>(null);

  protected readonly periodOptions: { label: string; value: PeriodPreset }[] = [
    { label: 'Último mês', value: 'last-month' },
    { label: '3 últimos meses', value: 'last-three-months' },
    { label: '6 últimos meses', value: 'last-six-months' },
    { label: 'Último ano', value: 'last-year' },
    { label: 'Selecionar período', value: 'custom' },
  ];

  protected selectedPeriod: PeriodPreset = 'last-month';
  protected startDate = this.subtractMonths(new Date(), 1);
  protected endDate = new Date();

  protected dailyRevenueData: ChartData<'line'> = { labels: [], datasets: [] };
  protected categoryRevenueData: ChartData<'bar'> = { labels: [], datasets: [] };
  protected statusData: ChartData<'doughnut'> = { labels: [], datasets: [] };

  protected dailyRevenueOptions: ChartOptions<'line'> = {};
  protected categoryRevenueOptions: ChartOptions<'bar'> = {};
  protected statusOptions: ChartOptions<'doughnut'> = {};

  ngOnInit(): void {
    this.configureChartOptions();
    this.loadReports();
  }

  protected onPeriodChange(): void {
    if (this.selectedPeriod === 'custom') {
      return;
    }

    const endDate = new Date();
    const monthsByPeriod: Record<Exclude<PeriodPreset, 'custom'>, number> = {
      'last-month': 1,
      'last-three-months': 3,
      'last-six-months': 6,
      'last-year': 12,
    };

    this.startDate = this.subtractMonths(endDate, monthsByPeriod[this.selectedPeriod]);
    this.endDate = endDate;
  }

  protected loadReports(): void {
    if (!this.startDate || !this.endDate) {
      this.notificationService.warning(
        'Informe o período',
        'Selecione as datas inicial e final do relatório.',
      );
      return;
    }

    if (this.startDate > this.endDate) {
      this.notificationService.warning(
        'Período inválido',
        'A data inicial deve ser anterior ou igual à data final.',
      );
      return;
    }

    this.isLoading.set(true);

    this.reportsService.loadDashboard(this.startDate, this.endDate).subscribe({
      next: (dashboard) => {
        this.dashboard.set(dashboard);
        this.updateChartData(dashboard);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.notificationService.error(
          'Erro ao gerar relatório',
          'Não foi possível carregar os dados dos relatórios.',
        );
      },
    });
  }

  protected goBack(): void {
    this.location.back();
  }

  private updateChartData(dashboard: ReportsDashboard): void {
    const colors = this.chartColors();

    this.dailyRevenueData = {
      labels: dashboard.revenueReport.dailyRevenue.map((item) => this.formatDate(item.date)),
      datasets: [
        {
          label: 'Receita confirmada',
          data: dashboard.revenueReport.dailyRevenue.map((item) => item.totalRevenue),
          borderColor: colors.primary,
          backgroundColor: colors.primarySoft,
          pointBackgroundColor: colors.primary,
          pointBorderColor: '#ffffff',
          pointBorderWidth: 2,
          pointRadius: 4,
          pointHoverRadius: 6,
          pointHoverBackgroundColor: colors.primaryHover,
          fill: true,
          tension: 0.35,
        },
      ],
    };

    this.categoryRevenueData = {
      labels: dashboard.categoryRevenue.map((item) => item.categoryName),
      datasets: [
        {
          label: 'Chamados pagos',
          data: dashboard.categoryRevenue.map((item) => item.paidRequestsCount),
          backgroundColor: '#60a5fa',
          borderColor: '#3b82f6',
          borderWidth: 0,
          borderRadius: 6,
          maxBarThickness: 30,
          yAxisID: 'requests',
        },
        {
          label: 'Receita',
          data: dashboard.categoryRevenue.map((item) => item.totalRevenue),
          backgroundColor: '#14b8a6',
          borderColor: '#0d9488',
          borderWidth: 0,
          borderRadius: 6,
          maxBarThickness: 30,
          yAxisID: 'revenue',
        },
      ],
    };

    this.statusData = {
      labels: dashboard.statusSummary.map((item) => item.status),
      datasets: [
        {
          data: dashboard.statusSummary.map((item) => item.requestsCount),
          backgroundColor: [
            '#60a5fa',
            '#fbbf24',
            '#22c55e',
            '#f43f5e',
            '#a78bfa',
            '#06b6d4',
            '#fb923c',
            '#10b981',
          ],
          borderColor: 'transparent',
          borderWidth: 0,
          hoverBorderWidth: 0,
          hoverOffset: 7,
          spacing: 2,
        },
      ],
    };
  }

  private configureChartOptions(): void {
    const colors = this.chartColors();
    const legend = {
      labels: {
        color: colors.textMuted,
        usePointStyle: true,
        boxWidth: 10,
        padding: 16,
      },
    };

    this.dailyRevenueOptions = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend,
        tooltip: {
          callbacks: {
            label: (context) => `Receita: ${this.formatCurrency(context.parsed.y ?? 0)}`,
          },
        },
      },
      scales: {
        x: {
          ticks: { color: colors.textMuted },
          grid: { display: false },
          border: { display: false },
        },
        y: {
          beginAtZero: true,
          ticks: {
            color: colors.textMuted,
            callback: (value) => this.formatCompactCurrency(Number(value)),
          },
          grid: { color: colors.border },
          border: { display: false },
        },
      },
    };

    this.categoryRevenueOptions = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend,
        tooltip: {
          callbacks: {
            label: (context) =>
              context.dataset.yAxisID === 'revenue'
                ? `Receita: ${this.formatCurrency(context.parsed.y ?? 0)}`
                : `Chamados pagos: ${context.parsed.y ?? 0}`,
          },
        },
      },
      scales: {
        x: {
          ticks: { color: colors.textMuted },
          grid: { display: false },
          border: { display: false },
        },
        requests: {
          type: 'linear',
          position: 'left',
          beginAtZero: true,
          ticks: { color: colors.textMuted, precision: 0, stepSize: 1 },
          grid: { color: colors.border },
          border: { display: false },
        },
        revenue: {
          type: 'linear',
          position: 'right',
          beginAtZero: true,
          ticks: {
            color: colors.textMuted,
            callback: (value) => this.formatCompactCurrency(Number(value)),
          },
          grid: { drawOnChartArea: false },
          border: { display: false },
        },
      },
    };

    this.statusOptions = {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '52%',
      plugins: {
        legend: { ...legend, position: 'bottom' },
      },
    };
  }

  private chartColors() {
    return {
      primary: '#14b8a6',
      primaryHover: '#0d9488',
      primarySoft: 'rgba(20, 184, 166, 0.16)',
      textMuted: '#64748b',
      border: 'rgba(148, 163, 184, 0.24)',
    };
  }

  private subtractMonths(value: Date, months: number): Date {
    const result = new Date(value);
    const originalDay = result.getDate();

    result.setDate(1);
    result.setMonth(result.getMonth() - months);

    const lastDayOfTargetMonth = new Date(result.getFullYear(), result.getMonth() + 1, 0).getDate();

    result.setDate(Math.min(originalDay, lastDayOfTargetMonth));
    return result;
  }

  private formatDate(value: string): string {
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      timeZone: 'UTC',
    }).format(new Date(`${value}T00:00:00Z`));
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  private formatCompactCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      notation: 'compact',
      maximumFractionDigits: 1,
    }).format(value);
  }
}
