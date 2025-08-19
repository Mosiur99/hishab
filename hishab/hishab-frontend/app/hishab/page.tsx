"use client";

import { useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { CostService, CreateCostRequest } from "../services/costService";
import { ItemService, Item } from "../services/itemService";
import { AuthService } from "../services/authService";

type Expense = {
  id: number;
  amount: number;
  description: string;
  category: string;
  date: string;
  paymentType?: string;
  costingType?: string;
};

type CostSummary = {
  result: boolean;
  message: string;
  todayTotal: number;
  monthTotal: number;
  dailyAverage: number;
  topCategory: string;
};

export default function HishabPage() {
  const router = useRouter();
  const [items, setItems] = useState<Item[]>([]);
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [stats, setStats] = useState<CostSummary>({
    result: false,
    message: "",
    todayTotal: 0,
    monthTotal: 0,
    dailyAverage: 0,
    topCategory: "",
  });
  const [isLoading, setIsLoading] = useState(true);
  const [showAddExpense, setShowAddExpense] = useState(false);
  
  // Form state for cost entry
  const [itemId, setItemId] = useState<number | null>(null);
  const [quantity, setQuantity] = useState("");
  const [perUnitCost, setPerUnitCost] = useState("");
  const [totalCost, setTotalCost] = useState("");
  const [weight, setWeight] = useState("");
  const [perWeightCost, setPerWeightCost] = useState("");
  const [paymentType, setPaymentType] = useState<'CASH' | 'CARD' | 'MOBILE_BANKING' | 'BANK_TRANSFER'>('CASH');
  const [costingType, setCostingType] = useState<'WEIGHT' | 'QUANTITY'>('QUANTITY');
  const [costingDate, setCostingDate] = useState(new Date().toLocaleDateString('en-CA'));
//   const [costingDate, setCostingDate] = useState(() => {
//   const today = new Date();
//   const year = today.getFullYear();
//   const month = String(today.getMonth() + 1).padStart(2, '0'); // months are 0-based
//   const day = String(today.getDate()).padStart(2, '0');
//   return `${year}-${month}-${day}`;
// });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [paymentTypeFilter, setPaymentTypeFilter] = useState<string>('ALL');

  // Check authentication on component mount
  useEffect(() => {
    if (!AuthService.isAuthenticated()) {
      router.push('/login');
      return;
    }
    
    if (!AuthService.isUser()) {
      router.push('/admin');
      return;
    }
  }, [router]);

  // Fetch items and expenses on component mount
  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch items
        const itemsResponse = await ItemService.getItems();
        if (itemsResponse.result && itemsResponse.data) {
          setItems(itemsResponse.data);
        }

        const costSummaryResponse = await CostService.getCostSummary();
        if (costSummaryResponse.result) {
          setStats(costSummaryResponse);
        }

        // Mock expenses data for demonstration
        const mockExpenses: Expense[] = [
          { id: 1, amount: 25.50, description: "Lunch", category: "Food", date: "2024-01-15", paymentType: "CASH", costingType: "QUANTITY" },
          { id: 2, amount: 15.00, description: "Coffee", category: "Food", date: "2024-01-15", paymentType: "CARD", costingType: "QUANTITY" },
          { id: 3, amount: 50.00, description: "Gas", category: "Transport", date: "2024-01-14", paymentType: "CASH", costingType: "WEIGHT" },
          { id: 4, amount: 1200.00, description: "Rent", category: "Housing", date: "2024-01-01", paymentType: "BANK_TRANSFER", costingType: "QUANTITY" },
          { id: 5, amount: 80.00, description: "Shopping", category: "Shopping", date: "2024-01-13", paymentType: "MOBILE_BANKING", costingType: "QUANTITY" },
        ];
        setExpenses(mockExpenses);

        // Calculate stats
        const today = new Date().toISOString().split('T')[0];
        const todayTotal = mockExpenses
          .filter(exp => exp.date === today)
          .reduce((sum, exp) => sum + exp.amount, 0);
        
        const currentMonth = new Date().getMonth();
        const monthTotal = mockExpenses
          .filter(exp => new Date(exp.date).getMonth() === currentMonth)
          .reduce((sum, exp) => sum + exp.amount, 0);

        const categoryCounts = mockExpenses.reduce((acc, exp) => {
          acc[exp.category] = (acc[exp.category] || 0) + 1;
          return acc;
        }, {} as Record<string, number>);

        const topCategory = Object.entries(categoryCounts)
          .sort(([,a], [,b]) => b - a)[0]?.[0] || "None";

      } catch (error) {
        console.error("Failed to fetch data:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleAddExpense = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    if (!itemId || !totalCost || isSubmitting) return;
    
    // Validate based on costing type
    if (costingType === 'QUANTITY' && (!quantity || !perUnitCost)) return;
    if (costingType === 'WEIGHT' && (!weight || !perWeightCost)) return;
    
    setIsSubmitting(true);
    try {
      const costRequest: CreateCostRequest = {
        itemId,
        quantity: parseInt(quantity),
        perUnitCost: parseFloat(perUnitCost),
        totalCost: parseFloat(totalCost),
        weight: weight ? parseFloat(weight) : undefined,
        perWeightCost: perWeightCost ? parseFloat(perWeightCost) : undefined,
        paymentType,
        costingType,
        costingDate
      };

      const response = await CostService.createCost(costRequest);
      
      if (response.result) {
        // Create a new expense entry for display
        const selectedItem = items.find(i => i.id === itemId);
        
        const newExpense: Expense = {
          id: expenses.length + 1,
          amount: parseFloat(totalCost),
          description: selectedItem?.name || "Unknown Item",
          category: selectedItem?.categoryType || "Unknown",
          date: costingDate,
          paymentType: paymentType,
          costingType: costingType
        };

        setExpenses(prev => [newExpense, ...prev]);
        
        // Reset form
        setItemId(null);
        setQuantity("");
        setPerUnitCost("");
        setTotalCost("");
        setWeight("");
        setPerWeightCost("");
        setPaymentType('CASH');
        setCostingType('QUANTITY');
        setCostingDate(new Date().toLocaleDateString('en-CA'));
        setShowAddExpense(false);
      } else {
        console.error("Failed to create cost:", response.message);
        alert("Failed to create cost entry: " + response.message);
      }

    } catch (error) {
      console.error("Failed to add expense:", error);
      alert("Failed to create cost entry. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  }, [itemId, quantity, perUnitCost, totalCost, weight, perWeightCost, paymentType, costingType, costingDate, isSubmitting, items, expenses]);

  // Calculate total cost automatically
  useEffect(() => {
    if (costingType === 'QUANTITY' && quantity && perUnitCost) {
      const calculatedTotal = parseFloat(quantity) * parseFloat(perUnitCost);
      setTotalCost(calculatedTotal.toFixed(2));
    } else if (costingType === 'WEIGHT' && weight && perWeightCost) {
      const calculatedTotal = parseFloat(weight) * parseFloat(perWeightCost);
      setTotalCost(calculatedTotal.toFixed(2));
    }
  }, [costingType, quantity, perUnitCost, weight, perWeightCost]);

  const handleLogout = () => {
    AuthService.logout();
    router.push('/login');
  };

  if (isLoading) {
    return (
      <div className="container-page px-6 py-10">
        <div className="flex items-center justify-center min-h-[400px]">
          <div className="text-lg text-gray-600">Loading dashboard...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="container-page px-6 py-10">
      {/* Header */}
      <div className="mb-8 flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Expense Dashboard</h1>
          <p className="mt-2 text-gray-600">Track and manage your daily expenses</p>
        </div>
        <button
          onClick={handleLogout}
          className="btn bg-red-600 hover:bg-red-700 focus:ring-red-500"
        >
          Logout
        </button>
      </div>

      {/* Quick Actions */}
      <div className="mb-8 flex flex-wrap gap-4">
        <button
          onClick={() => setShowAddExpense(true)}
          className="btn bg-green-600 hover:bg-green-700 focus:ring-green-500"
        >
          + Add Daily Cost Entry
        </button>
        <button
          onClick={() => router.push('/monthly-report')}
          className="btn bg-blue-600 hover:bg-blue-700 focus:ring-blue-500"
        >
          📊 Monthly Report
        </button>
        <button
          onClick={() => router.push('/expense-history')}
          className="btn bg-purple-600 hover:bg-purple-700 focus:ring-purple-500"
        >
          📋 Expense History
        </button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-2 bg-green-100 rounded-lg">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Today's Total</p>
              <p className="text-2xl font-bold text-gray-900">Tk. {stats.todayTotal.toFixed(2)}</p>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-2 bg-blue-100 rounded-lg">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">This Month</p>
              <p className="text-2xl font-bold text-gray-900">Tk. {stats.monthTotal.toFixed(2)}</p>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-2 bg-purple-100 rounded-lg">
              <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Daily Average</p>
              <p className="text-2xl font-bold text-gray-900">Tk. {stats.dailyAverage.toFixed(2)}</p>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-2 bg-orange-100 rounded-lg">
              <svg className="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Top Category</p>
              <p className="text-2xl font-bold text-gray-900">{stats.topCategory}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Add Expense Modal */}
      {showAddExpense && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="card max-w-2xl w-full p-6 max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold">Add Daily Cost Entry</h3>
              <button
                onClick={() => setShowAddExpense(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <form onSubmit={handleAddExpense} className="space-y-4">
              <div>
                <label htmlFor="item" className="label">Item *</label>
                <select
                  id="item"
                  className="input"
                  value={itemId || ""}
                  onChange={(e) => setItemId(Number(e.target.value) || null)}
                  required
                >
                  <option value="">Select an item ({items.length} items available)</option>
                  {items.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.name} - {item.categoryType}
                    </option>
                  ))}
                </select>
              </div>

              {costingType === 'QUANTITY' ? (
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="quantity" className="label">Quantity *</label>
                    <input
                      id="quantity"
                      type="number"
                      min="1"
                      className="input"
                      placeholder="1"
                      value={quantity}
                      onChange={(e) => setQuantity(e.target.value)}
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="perUnitCost" className="label">Per Unit Cost *</label>
                    <input
                      id="perUnitCost"
                      type="number"
                      step="0.01"
                      min="0"
                      className="input"
                      placeholder="0.00"
                      value={perUnitCost}
                      onChange={(e) => setPerUnitCost(e.target.value)}
                      required
                    />
                  </div>
                </div>
              ) : (
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="weight" className="label">Weight *</label>
                    <input
                      id="weight"
                      type="number"
                      step="0.01"
                      min="0"
                      className="input"
                      placeholder="0.00"
                      value={weight}
                      onChange={(e) => setWeight(e.target.value)}
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="perWeightCost" className="label">Per Weight Cost *</label>
                    <input
                      id="perWeightCost"
                      type="number"
                      step="0.01"
                      min="0"
                      className="input"
                      placeholder="0.00"
                      value={perWeightCost}
                      onChange={(e) => setPerWeightCost(e.target.value)}
                      required
                    />
                  </div>
                </div>
              )}

              <div>
                <label htmlFor="totalCost" className="label">Total Cost *</label>
                <input
                  id="totalCost"
                  type="number"
                  step="0.01"
                  min="0"
                  className="input"
                  placeholder="0.00"
                  value={totalCost}
                  onChange={(e) => setTotalCost(e.target.value)}
                  required
                />
              </div>

              <div>
                <label htmlFor="costingType" className="label">Costing Type *</label>
                <select
                  id="costingType"
                  className="input"
                  value={costingType}
                  onChange={(e) => setCostingType(e.target.value as 'WEIGHT' | 'QUANTITY')}
                  required
                >
                  <option value="QUANTITY">By Quantity</option>
                  <option value="WEIGHT">By Weight</option>
                </select>
              </div>

              <div>
                <label htmlFor="paymentType" className="label">Purchase Type *</label>
                <select
                  id="paymentType"
                  className="input"
                  value={paymentType}
                  onChange={(e) => setPaymentType(e.target.value as 'CASH' | 'CARD' | 'MOBILE_BANKING' | 'BANK_TRANSFER')}
                  required
                >
                  <option value="CASH">Cash</option>
                  <option value="CARD">Card</option>
                  <option value="MOBILE_BANKING">Mobile Banking</option>
                  <option value="BANK_TRANSFER">Bank Transfer</option>
                </select>
              </div>

              <div>
                <label htmlFor="costingDate" className="label">Costing Date *</label>
                <input
                  id="costingDate"
                  type="date"
                  className="input"
                  value={costingDate}
                  onChange={(e) => setCostingDate(e.target.value)}
                  required
                />
              </div>

              <div className="flex gap-3 pt-4">
                <button
                  type="submit"
                  className="btn flex-1"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? "Creating..." : "Create Cost Entry"}
                </button>
                <button
                  type="button"
                  onClick={() => setShowAddExpense(false)}
                  className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900"
                  disabled={isSubmitting}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
