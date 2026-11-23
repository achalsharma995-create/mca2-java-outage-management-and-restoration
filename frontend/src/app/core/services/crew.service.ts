import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Crew } from '../models/crew.model';

@Injectable({
  providedIn: 'root'
})
export class CrewService {
  private readonly apiUrl = 'http://localhost:8080/api/crews';

  constructor(private http: HttpClient) {}

  getAllCrews(): Observable<Crew[]> {
    return this.http.get<Crew[]>(this.apiUrl);
  }

  getAvailableCrews(): Observable<Crew[]> {
    return this.http.get<Crew[]>(`${this.apiUrl}/available`);
  }

  getCrewById(id: number): Observable<Crew> {
    return this.http.get<Crew>(`${this.apiUrl}/${id}`);
  }

  createCrew(crew: Partial<Crew>): Observable<Crew> {
    return this.http.post<Crew>(this.apiUrl, crew);
  }

  updateCrew(id: number, crew: Partial<Crew>): Observable<Crew> {
    return this.http.put<Crew>(`${this.apiUrl}/${id}`, crew);
  }

  updateStatus(id: number, availabilityStatus: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/status`, { availabilityStatus });
  }
}
