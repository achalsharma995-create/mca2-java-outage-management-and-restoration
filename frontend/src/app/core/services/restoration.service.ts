import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProgressUpdate {
  id?: number;
  workOrderId: number;
  percentageComplete: number;
  statusReached: string;
  remarks: string;
  estimatedRestorationTime?: string;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class RestorationService {
  private readonly apiUrl = 'http://localhost:8080/api/progress';

  constructor(private http: HttpClient) {}

  getTimeline(outageId: number): Observable<ProgressUpdate[]> {
    return this.http.get<ProgressUpdate[]>(`${this.apiUrl}/outage/${outageId}/timeline`);
  }

  addProgress(update: ProgressUpdate): Observable<ProgressUpdate> {
    return this.http.post<ProgressUpdate>(this.apiUrl, update);
  }
}
