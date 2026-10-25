import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import { MessageService } from 'primeng/api';

import { HttpAuthApiClient } from './core/api/http-auth-api-client';
import { EquipmentCategoryApiClient } from './features/equipment-categories/api/equipment-category-api-client';
import { HttpEquipmentCategoryApiClient } from './features/equipment-categories/api/http-equipment-category-api-client';
import { MaintenanceRequestApiClient } from './features/maintenance-requests/api/maintenance-request-api-client';
import { HttpMaintenanceRequestApiClient } from './features/maintenance-requests/api/http-maintenance-request-api-client';
import { EmployeeApiClient } from './features/employees/api/employee-api-client';
import { HttpEmployeeApiClient } from './features/employees/api/http-employee-api-client';
import { AuthApiClient } from './core/api/auth-api-client';
import { routes } from './app.routes';
import { authInterceptor } from './core/auth/auth.interceptor';


registerLocaleData(localePt);

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    { provide: EquipmentCategoryApiClient, useClass: HttpEquipmentCategoryApiClient },
    { provide: MaintenanceRequestApiClient, useClass: HttpMaintenanceRequestApiClient },
    { provide: EmployeeApiClient, useClass: HttpEmployeeApiClient },
    { provide: AuthApiClient, useClass: HttpAuthApiClient },
    MessageService,
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideRouter(routes),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: false,
        },
      },
      license:
        'eyJpZCI6IjVkOWUyOWQwLTc3OGUtNDBhYS1hN2YxLWYzYzBhNDYyOTlhNSIsInByb2R1Y3QiOiJwcmltZXVpIiwidGllciI6ImNvbW11bml0eSIsInR5cGUiOiJkZXYiLCJpYXQiOjE3ODc5Mzc5OTIsImV4cCI6MTgxOTQ3Mzk5Mn0.px1BHimOEwS18-OrZEKv9xigMiMb7uDmqhO_SabTnr7YoVdm1lsv4ken2KNz935y_Xywkg8bRGO4XvTuEaxEAg',
    }),
  ],
};
