import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should authenticate user and store token in localStorage', () => {
    const mockResponse = {
      accessToken: 'sample-jwt-token',
      tokenType: 'Bearer',
      userId: 1,
      username: 'admin',
      email: 'admin@gridflow.energy',
      fullName: 'Alex Morgan',
      roles: ['ROLE_ADMIN']
    };

    service.login({ username: 'admin', password: 'password' }).subscribe(res => {
      expect(res.accessToken).toBe('sample-jwt-token');
      expect(localStorage.getItem('omrs_token')).toBe('sample-jwt-token');
      expect(service.isAuthenticated()).toBeTrue();
    });

    const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  it('should clear token and user on logout', () => {
    localStorage.setItem('omrs_token', 'temp-token');
    service.logout();
    expect(localStorage.getItem('omrs_token')).toBeNull();
    expect(service.currentUserValue).toBeNull();
  });
});
