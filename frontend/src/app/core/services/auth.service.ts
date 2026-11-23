import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { AuthResponse, User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  constructor(private http: HttpClient) {
    const savedUser = localStorage.getItem('omrs_user');
    this.currentUserSubject = new BehaviorSubject<User | null>(savedUser ? JSON.parse(savedUser) : null);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  public get token(): string | null {
    return localStorage.getItem('omrs_token');
  }

  public isAuthenticated(): boolean {
    return !!this.token;
  }

  public hasRole(role: string): boolean {
    const user = this.currentUserValue;
    return !!user && user.roles.includes(role);
  }

  login(credentials: { username: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      map(res => {
        localStorage.setItem('omrs_token', res.accessToken);
        const user: User = {
          id: res.userId,
          username: res.username,
          email: res.email,
          fullName: res.fullName,
          roles: res.roles
        };
        localStorage.setItem('omrs_user', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return res;
      })
    );
  }

  logout(): void {
    localStorage.removeItem('omrs_token');
    localStorage.removeItem('omrs_user');
    this.currentUserSubject.next(null);
  }
}
