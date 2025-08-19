"use client";

import { useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { AuthService } from "../services/authService";
import { CostService, RecentExpenseDTO, RecentExpensesResponse } from "../services/costService";

export default function RecentExpensesPage() {
  const router = useRouter();
  const [expenses, setExpenses] = useState<RecentExpenseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;

  // Filter states
  const [paymentTypeFilter, setPaymentTypeFilter] = useState("ALL");
  const [costingTypeFilter, setCostingTypeFilter] = useState("ALL");

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

  const fetchRecentExpenses = useCallback(async (page: number = 0) => {
    setIsLoading(true);
    try {
      const response = await CostService.getRecentExpenses(page, pageSize);
      if (response.result) {
        setExpenses(response.recentExpenses);
        setTotalElements(response.totalElements);
        const totalPagesCalc = Math.ceil(response.totalElements / response.pageSize);
        setTotalPages(totalPagesCalc);
        setCurrentPage(response.pageNo);
      } else {
        console.error('Failed to fetch recent expenses:', response.message);
      }
    } catch (error) {
      console.error('Error fetching recent expenses:', error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchRecentExpenses(0);
  }, [fetchRecentExpenses]);

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
      fetchRecentExpenses(newPage);
    }
  };

  const getPaymentTypeIcon = (paymentType: string) => {
    switch (paymentType) {
      case 'CASH': return '💵';
      case 'CARD': return '💳';
      case 'MOBILE_BANKING': return '📱';
      case 'BANK_TRANSFER': return '🏦';
      default: return '💰';
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  const formatDateTime = (dateString: string) => {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const filteredExpenses = expenses.filter(expense => {
    if (paymentTypeFilter !== 'ALL' && expense.paymentType !== paymentTypeFilter) return false;
    if (costingTypeFilter !== 'ALL' && expense.costingType !== costingTypeFilter) return false;
    return true;
  });

  if (isLoading && expenses.length === 0) {
    return (
      <div className="container-page px-6 py-10">
        <div className="flex items-center justify-center min-h-[400px]">
          <div className="text-lg text-gray-600">Loading recent expenses...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="container-page px-6 py-10">
      {/* Header */}
      <div className="mb-8 flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Recent Expenses</h1>
          <p className="mt-2 text-gray-600">View your latest expense entries</p>
        </div>
        <button
          onClick={() => router.push('/hishab')}
          className="btn bg-blue-600 hover:bg-blue-700 focus:ring-blue-500"
        >
          ← Back to Dashboard
        </button>
      </div>

      {/* Filters */}
      <div className="card p-6 mb-8">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Filters</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label htmlFor="paymentType" className="label">Payment Type</label>
            <select
              id="paymentType"
              className="input"
              value={paymentTypeFilter}
              onChange={(e) => setPaymentTypeFilter(e.target.value)}
            >
              <option value="ALL">All Payment Types</option>
              <option value="CASH">Cash</option>
              <option value="CARD">Card</option>
              <option value="MOBILE_BANKING">Mobile Banking</option>
              <option value="BANK_TRANSFER">Bank Transfer</option>
            </select>
          </div>
          
          <div>
            <label htmlFor="costingType" className="label">Costing Type</label>
            <select
              id="costingType"
              className="input"
              value={costingTypeFilter}
              onChange={(e) => setCostingTypeFilter(e.target.value)}
            >
              <option value="ALL">All Costing Types</option>
              <option value="QUANTITY">By Quantity</option>
              <option value="WEIGHT">By Weight</option>
            </select>
          </div>
        </div>
      </div>

      {/* Results Summary */}
      <div className="mb-6">
        <p className="text-sm text-gray-600">
          Showing {filteredExpenses.length} of {totalElements} expenses
        </p>
      </div>

      {/* Expense List */}
      <div className="card p-6 mb-8">
        {filteredExpenses.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-gray-400 mb-4">
              <svg className="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">No expenses found</h3>
            <p className="text-gray-600">Try adjusting your filters or add some expenses first.</p>
          </div>
        ) : (
          <div className="space-y-4">
            {filteredExpenses.map((expense) => (
              <div key={expense.id} className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors">
                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-4">
                    <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                      <span className="text-blue-600 font-semibold text-lg">
                        {expense.categoryType.charAt(0).toUpperCase()}
                      </span>
                    </div>
                    <div className="flex-1">
                      <h3 className="font-semibold text-gray-900">{expense.itemName}</h3>
                      <div className="flex flex-wrap items-center gap-2 mt-1 text-sm text-gray-600">
                        <span className="inline-flex items-center px-2 py-1 rounded-full bg-gray-100 text-xs">
                          {expense.categoryType}
                        </span>
                        <span className="inline-flex items-center px-2 py-1 rounded-full bg-blue-100 text-blue-800 text-xs">
                          {expense.costingType}
                        </span>
                        <span className="inline-flex items-center px-2 py-1 rounded-full bg-green-100 text-green-800 text-xs">
                          {getPaymentTypeIcon(expense.paymentType)} {expense.paymentType.replace('_', ' ')}
                        </span>
                      </div>
                      
                      <div className="mt-2 text-sm text-gray-600">
                        {expense.costingType === 'QUANTITY' ? (
                          <span>
                            Quantity: {expense.quantity} × Tk. {expense.perUnitCost?.toFixed(2)}
                          </span>
                        ) : (
                          <span>
                            Weight: {expense.weight} × Tk. {expense.perWeightCost?.toFixed(2)}
                          </span>
                        )}
                      </div>
                      
                      <div className="mt-1 text-sm text-gray-500">
                        <span>Purchase Date: {formatDate(expense.costingDate)}</span>
                        <span className="mx-2">•</span>
                        <span>Added: {formatDateTime(expense.created)}</span>
                      </div>
                    </div>
                  </div>
                  
                  <div className="text-right">
                    <p className="text-2xl font-bold text-gray-900">Tk. {expense.totalCost.toFixed(2)}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex items-center justify-between">
          <div className="text-sm text-gray-600">
            Page {currentPage + 1} of {totalPages}
          </div>
          
          <div className="flex items-center space-x-2">
            <button
              onClick={() => handlePageChange(0)}
              disabled={currentPage === 0}
              className="px-3 py-2 text-sm border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              First
            </button>
            
            <button
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 0}
              className="px-3 py-2 text-sm border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              Previous
            </button>
            
            <div className="flex items-center space-x-1">
              {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                let pageNum;
                if (totalPages <= 5) {
                  pageNum = i;
                } else if (currentPage < 3) {
                  pageNum = i;
                } else if (currentPage >= totalPages - 3) {
                  pageNum = totalPages - 5 + i;
                } else {
                  pageNum = currentPage - 2 + i;
                }
                
                return (
                  <button
                    key={pageNum}
                    onClick={() => handlePageChange(pageNum)}
                    className={`px-3 py-2 text-sm border rounded-md ${
                      pageNum === currentPage
                        ? 'bg-blue-600 text-white border-blue-600'
                        : 'border-gray-300 hover:bg-gray-50'
                    }`}
                  >
                    {pageNum + 1}
                  </button>
                );
              })}
            </div>
            
            <button
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage >= totalPages - 1}
              className="px-3 py-2 text-sm border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              Next
            </button>
            
            <button
              onClick={() => handlePageChange(totalPages - 1)}
              disabled={currentPage >= totalPages - 1}
              className="px-3 py-2 text-sm border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              Last
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
