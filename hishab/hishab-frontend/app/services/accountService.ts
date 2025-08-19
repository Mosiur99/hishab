export interface Account {
  id: number;
  accountName: string;
  accountType: string;
  balance?: number;
  description?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
}

export class AccountService {
  private static baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  static async getAccounts(): Promise<ApiResponse<Account[]>> {
    try {
      const response = await fetch(`${this.baseUrl}/api/v1/admin/account/list`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error('Error fetching accounts:', error);
      return {
        success: false,
        message: 'Failed to fetch accounts',
        error: error instanceof Error ? error.message : 'Unknown error',
      };
    }
  }
}
