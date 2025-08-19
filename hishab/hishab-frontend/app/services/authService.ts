export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  email: string;
  password: string;
  name: string;
}

export interface GoogleAuthRequest {
  idToken: string;
  email: string;
  name: string;
  googleId: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  token?: string;
  refreshToken?: string;
  user?: UserInfo;
}

export interface UserInfo {
  id: number;
  email: string;
  name: string;
  role: 'USER' | 'ADMIN';
  emailVerified: boolean;
}

export class AuthService {
  private static baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  static async login(request: LoginRequest): Promise<AuthResponse> {
    try {
      const response = await fetch(`${this.baseUrl}/api/v1/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      if (result.success && result.token) {
        localStorage.setItem('token', result.token);
        localStorage.setItem('refreshToken', result.refreshToken);
        localStorage.setItem('user', JSON.stringify(result.user));
      }
      return result;
    } catch (error) {
      console.error('Error during login:', error);
      return {
        success: false,
        message: 'Failed to login: ' + (error instanceof Error ? error.message : 'Unknown error'),
      };
    }
  }

  static async signup(request: SignupRequest): Promise<AuthResponse> {
    try {
      const response = await fetch(`${this.baseUrl}/api/v1/auth/signup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      if (result.success && result.token) {
        localStorage.setItem('token', result.token);
        localStorage.setItem('refreshToken', result.refreshToken);
        localStorage.setItem('user', JSON.stringify(result.user));
      }
      return result;
    } catch (error) {
      console.error('Error during signup:', error);
      return {
        success: false,
        message: 'Failed to signup: ' + (error instanceof Error ? error.message : 'Unknown error'),
      };
    }
  }

  static async googleAuth(request: GoogleAuthRequest): Promise<AuthResponse> {
    try {
      const response = await fetch(`${this.baseUrl}/api/v1/auth/google`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      if (result.success && result.token) {
        localStorage.setItem('token', result.token);
        localStorage.setItem('refreshToken', result.refreshToken);
        localStorage.setItem('user', JSON.stringify(result.user));
      }
      return result;
    } catch (error) {
      console.error('Error during Google auth:', error);
      return {
        success: false,
        message: 'Failed to authenticate with Google: ' + (error instanceof Error ? error.message : 'Unknown error'),
      };
    }
  }

  static async logout(): Promise<AuthResponse> {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const response = await fetch(`${this.baseUrl}/api/v1/auth/logout`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
      }

      // Clear local storage
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');

      return { success: true, message: 'Logged out successfully' };
    } catch (error) {
      console.error('Error during logout:', error);
      // Still clear local storage even if API call fails
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      return { success: true, message: 'Logged out successfully' };
    }
  }

  static async refreshToken(): Promise<AuthResponse> {
    try {
      const refreshToken = localStorage.getItem('refreshToken');
      if (!refreshToken) {
        throw new Error('No refresh token available');
      }

      const response = await fetch(`${this.baseUrl}/api/v1/auth/refresh-token`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ refreshToken }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      if (result.success && result.token) {
        localStorage.setItem('token', result.token);
        localStorage.setItem('refreshToken', result.refreshToken);
        localStorage.setItem('user', JSON.stringify(result.user));
      }
      return result;
    } catch (error) {
      console.error('Error refreshing token:', error);
      // Clear invalid tokens
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      return {
        success: false,
        message: 'Failed to refresh token: ' + (error instanceof Error ? error.message : 'Unknown error'),
      };
    }
  }

  static getToken(): string | null {
    return localStorage.getItem('token');
  }

  static getUser(): UserInfo | null {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  static isAuthenticated(): boolean {
    return !!this.getToken();
  }

  static isAdmin(): boolean {
    const user = this.getUser();
    return user?.role === 'ADMIN';
  }

  static isUser(): boolean {
    const user = this.getUser();
    return user?.role === 'USER';
  }
}
