import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import { MessageService } from 'primeng/api';

import { AuthApiClient } from './core/api/auth-api-client';
import { MockAuthApiClient } from './core/api/mock-auth-api-client';
import { routes } from './app.routes';

registerLocaleData(localePt);

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    { provide: AuthApiClient, useClass: MockAuthApiClient },
    MessageService,
    provideBrowserGlobalErrorListeners(),
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
