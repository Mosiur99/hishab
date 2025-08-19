export interface Item {
  id: number;
  name: string;
  categoryId: number;
  categoryType: string;
}

export interface ApiResponse<T> {
  result: boolean;
  message: string;
  data: T;
}

export class ItemService {
  private static baseUrl = 'http://localhost:8080';

  static async getItems(): Promise<ApiResponse<Item[]>> {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${this.baseUrl}/api/v1/user/items`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result: ApiResponse<Item[]> = await response.json();
      return result;
    } catch (error) {
      console.error('Error fetching items:', error);
      return {
        result: false,
        message: 'Failed to fetch items',
        data: [],
      };
    }
  }

  static async getItemsByCategory(categoryId: number): Promise<ApiResponse<Item[]>> {
    try {
      const response = await fetch(
        `${this.baseUrl}/api/v1/admin/item/list?categoryId=${categoryId}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result: ApiResponse<Item[]> = await response.json();
      return result;
    } catch (error) {
      console.error('Error fetching items by category:', error);
      return {
        result: false,
        message: 'Failed to fetch items by category',
        data: [],
      };
    }
  }
}
