import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserDTO {
  username: string;
  email?: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  register(user: UserDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user, {
      responseType: 'text',
    });
  }

  login(user: UserDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, user, {
      responseType: 'text',
    });
  }

  isAuthenticated(): boolean {
    if (typeof window !== 'undefined' && typeof sessionStorage !== 'undefined') {
      return !!sessionStorage.getItem('authToken');
    }
    return false;
  }

  logout(): void {
    if (typeof window !== 'undefined' && typeof sessionStorage !== 'undefined') {
      sessionStorage.removeItem('authToken');
    }
  }

  // New method to handle password change
  changePassword(currentPassword: string, newPassword: string): Observable<any> {
    const changePasswordData = { currentPassword, newPassword };
    return this.http.post(`${this.baseUrl}/change-password`, changePasswordData, {
      responseType: 'text',
    });
  }
}
