export interface Category {
  id: number;
  categoryType: string;
  description?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
}

export class CategoryService {
  private static baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  static async getCategories(): Promise<ApiResponse<Category[]>> {
    try {
      const response = await fetch(`${this.baseUrl}/api/v1/admin/category/list`, {
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
      console.error('Error fetching categories:', error);
      return {
        success: false,
        message: 'Failed to fetch categories',
        error: error instanceof Error ? error.message : 'Unknown error',
      };
    }
  }
}
