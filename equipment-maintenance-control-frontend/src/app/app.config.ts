import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
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
