import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles: string[] = route.data?.['roles'] || [];
  const user = authService.currentUserValue;

  if (user && user.roles.some(role => expectedRoles.includes(role))) {
    return true;
  }

  router.navigate(['/unauthorized']);
  return false;
};
