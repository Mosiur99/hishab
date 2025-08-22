export interface CreateCostRequest {
  itemId: number;
  quantity: number;
  perUnitCost: number;
  totalCost: number;
  weight?: number;
  perWeightCost?: number;
  paymentType: 'CASH' | 'CARD' | 'MOBILE_BANKING' | 'BANK_TRANSFER';
  costingType: 'WEIGHT' | 'QUANTITY';
  costingDate: string;
  description: string;
}

export interface CostSummary {
  result: boolean;
  message: string;
  todayTotal: number;
  monthTotal: number;
  dailyAverage: number;
  topCategory: string;
}

export interface ActionResponse {
  result: boolean;
  message: string;
  data?: any;
}

export interface DailyTotalDTO {
  date: string;
  total: number;
  itemsWithCost: string;
}

export interface MonthlyReport {
  result: boolean;
  message: string;
  year: number;
  month: number;
  monthTotal: number;
  dailyAverage: number;
  topCategory?: string;
  dailyTotals: DailyTotalDTO[];
}

export interface RecentExpenseDTO {
  id: number;
  itemName: string;
  categoryType: string;
  quantity?: number;
  perUnitCost?: number;
  totalCost: number;
  weight?: number;
  perWeightCost?: number;
  paymentType: string;
  costingType: string;
  costingDate: string;
  created: string;
}

export interface RecentExpensesResponse {
  result: boolean;
  message: string;
  recentExpenses: RecentExpenseDTO[];
  totalElements: number;
  pageNo: number;
  pageSize: number;
}

export class CostService {
  private static baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  static async createCost(request: CreateCostRequest): Promise<ActionResponse> {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${this.baseUrl}/api/v1/user/cost`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error('Error creating cost:', error);
      return {
        result: false,
        message: 'Failed to create cost entry: ' + (error instanceof Error ? error.message : 'Unknown error'),
      };
    }
  }

  static async getCostSummary(): Promise<CostSummary> {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${this.baseUrl}/api/v1/user/cost-summary`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      console.log(result);
      return result;
    } catch (error) {
      console.error('Error creating cost:', error);
      return {
        result: false,
        message: 'Failed to create cost entry: ' + (error instanceof Error ? error.message : 'Unknown error'),
        todayTotal: 0,
        monthTotal: 0,
        dailyAverage: 0,
        topCategory: "",
      };
    }
  }

  static async getMonthlyReport(year: number, month: number): Promise<MonthlyReport> {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found.");
      }

      const params = new URLSearchParams({ year: String(year), month: String(month) });
      const response = await fetch(`${this.baseUrl}/api/v1/user/monthly-report?${params}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result: MonthlyReport = await response.json();
      return result;
    } catch (error) {
      console.error('Error fetching monthly report:', error);

      // Returning a safe fallback structure
      return {
        result: false,
        message: error instanceof Error ? error.message : 'Unknown error occurred',
      } as MonthlyReport;
    }
  }
}
