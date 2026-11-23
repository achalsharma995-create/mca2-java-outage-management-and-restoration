import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NotificationItem {
  id: number;
  userId?: number;
  customerName?: string;
  eventType: string;
  title: string;
  message: string;
  channel: string;
  status: string;
  sentAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly apiUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) {}

  getMyNotifications(): Observable<NotificationItem[]> {
    return this.http.get<NotificationItem[]>(`${this.apiUrl}/my`);
  }

  getAllNotifications(): Observable<NotificationItem[]> {
    return this.http.get<NotificationItem[]>(this.apiUrl);
  }

  sendBroadcast(payload: { areaId: number; title: string; message: string; channel: string }): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/broadcast`, payload);
  }

  markAsRead(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/read`, {});
  }
}
