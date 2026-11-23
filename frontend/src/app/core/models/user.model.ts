export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  phoneNumber?: string;
  roles: string[];
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  userId: number;
  username: string;
  email: string;
  fullName: string;
  roles: string[];
}
