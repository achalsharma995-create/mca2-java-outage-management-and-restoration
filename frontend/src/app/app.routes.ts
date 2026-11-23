import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./modules/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'outages',
    canActivate: [authGuard],
    loadComponent: () => import('./modules/outages/outage-list/outage-list.component').then(m => m.OutageListComponent)
  },
  {
    path: 'crews',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_OPERATOR'] },
    loadComponent: () => import('./modules/crews/crew-list/crew-list.component').then(m => m.CrewListComponent)
  },
  {
    path: 'work-orders',
    canActivate: [authGuard],
    loadComponent: () => import('./modules/work-orders/work-order-list/work-order-list.component').then(m => m.WorkOrderListComponent)
  },
  {
    path: 'progress',
    canActivate: [authGuard],
    loadComponent: () => import('./modules/restoration/progress-tracker/progress-tracker.component').then(m => m.ProgressTrackerComponent)
  },
  {
    path: 'notifications',
    canActivate: [authGuard],
    loadComponent: () => import('./modules/notifications/notification-list/notification-list.component').then(m => m.NotificationListComponent)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
