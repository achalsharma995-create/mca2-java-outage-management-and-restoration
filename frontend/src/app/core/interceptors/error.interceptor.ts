import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if ([401, 403].includes(error.status) && authService.isAuthenticated()) {
        authService.logout();
        router.navigate(['/login']);
      }

      const errorMessage = error.error?.message || error.statusText || 'Server Error';
      console.error('[HTTP ERROR]', error.status, errorMessage);
      return throwError(() => new Error(errorMessage));
    })
  );
};
