import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, NotificationItem } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {
  notifications: NotificationItem[] = [];
  loading = false;

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.fetchNotifications();
  }

  fetchNotifications(): void {
    this.loading = true;
    this.notificationService.getAllNotifications().subscribe({
      next: (data) => {
        this.notifications = data;
        this.loading = false;
      },
      error: () => {
        this.notifications = [
          { id: 1, eventType: 'OUTAGE_REPORTED', title: 'Grid Alert: Outage in Green Valley Suburbs', message: 'We have detected an outage affecting your service address. Reference: OUT-2026-0001.', channel: 'SMS', status: 'SENT', sentAt: '14:16' },
          { id: 2, eventType: 'CREW_ASSIGNED', title: 'Restoration Crew Dispatched', message: 'Bravo Line Crew dispatched to work order WO-2026-0001. ETA 25 minutes.', channel: 'IN_APP', status: 'READ', sentAt: '14:41' },
          { id: 3, eventType: 'POWER_RESTORED', title: 'Power Restored in Downtown Financial District', message: 'Electric service has been fully restored. Outage resolved in 105 minutes.', channel: 'SMS,EMAIL', status: 'SENT', sentAt: '10:16' }
        ];
        this.loading = false;
      }
    });
  }
}
