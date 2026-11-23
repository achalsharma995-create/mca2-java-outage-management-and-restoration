import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkOrder, WorkOrderRequest } from '../models/work-order.model';

@Injectable({
  providedIn: 'root'
})
export class WorkOrderService {
  private readonly apiUrl = 'http://localhost:8080/api/work-orders';

  constructor(private http: HttpClient) {}

  getAllWorkOrders(): Observable<WorkOrder[]> {
    return this.http.get<WorkOrder[]>(this.apiUrl);
  }

  getWorkOrderById(id: number): Observable<WorkOrder> {
    return this.http.get<WorkOrder>(`${this.apiUrl}/${id}`);
  }

  createWorkOrder(request: WorkOrderRequest): Observable<WorkOrder> {
    return this.http.post<WorkOrder>(this.apiUrl, request);
  }

  assignCrew(workOrderId: number, crewId: number): Observable<WorkOrder> {
    return this.http.patch<WorkOrder>(`${this.apiUrl}/${workOrderId}/assign`, { crewId });
  }

  updateStatus(workOrderId: number, status: string, resolutionNotes?: string): Observable<WorkOrder> {
    return this.http.patch<WorkOrder>(`${this.apiUrl}/${workOrderId}/status`, { status, resolutionNotes });
  }
}
