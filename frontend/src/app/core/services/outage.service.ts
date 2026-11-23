import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Outage, OutageReportRequest } from '../models/outage.model';

@Injectable({
  providedIn: 'root'
})
export class OutageService {
  private readonly apiUrl = 'http://localhost:8080/api/outages';

  constructor(private http: HttpClient) {}

  getOutages(filters: { areaId?: number; status?: string; severity?: string; search?: string; page?: number; size?: number }): Observable<any> {
    let params = new HttpParams();
    if (filters.areaId) params = params.set('areaId', filters.areaId.toString());
    if (filters.status && filters.status !== 'ALL') params = params.set('status', filters.status);
    if (filters.severity && filters.severity !== 'ALL') params = params.set('severity', filters.severity);
    if (filters.search) params = params.set('search', filters.search);
    if (filters.page !== undefined) params = params.set('page', filters.page.toString());
    if (filters.size !== undefined) params = params.set('size', filters.size.toString());

    return this.http.get<any>(this.apiUrl, { params });
  }

  getOutageById(id: number): Observable<Outage> {
    return this.http.get<Outage>(`${this.apiUrl}/${id}`);
  }

  reportOutage(request: OutageReportRequest): Observable<Outage> {
    return this.http.post<Outage>(this.apiUrl, request);
  }

  updateStatus(id: number, status: string, rootCause?: string): Observable<Outage> {
    return this.http.patch<Outage>(`${this.apiUrl}/${id}/status`, { status, rootCause });
  }

  updateEtr(id: number, estimatedRestoreTime: string): Observable<Outage> {
    return this.http.patch<Outage>(`${this.apiUrl}/${id}/etr`, { estimatedRestoreTime });
  }
}
