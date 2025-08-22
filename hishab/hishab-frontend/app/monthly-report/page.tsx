"use client";

import { useEffect, useMemo, useState } from "react";
import React from "react";
import { useRouter } from "next/navigation";
import { AuthService } from "../services/authService";
import { CostService, MonthlyReport } from "../services/costService";

export default function MonthlyReportPage() {
  const router = useRouter();
  const [year, setYear] = useState<number>(new Date().getFullYear());
  const [month, setMonth] = useState<number>(new Date().getMonth() + 1);
  const [report, setReport] = useState<MonthlyReport | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

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

  useEffect(() => {
    const fetchReport = async () => {
      setLoading(true);
      const data = await CostService.getMonthlyReport(year, month);
      setReport(data);
      setLoading(false);
    };
    fetchReport();
  }, [year, month]);

  const monthName = useMemo(() => new Date(year, month - 1, 1).toLocaleString('en-US', { month: 'long' }), [year, month]);

  return (
    <div className="container-page px-6 py-10">
      <div className="mb-8 flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Monthly Report</h1>
          <p className="mt-2 text-gray-600">Summary and daily totals for the selected month</p>
        </div>
        <button onClick={() => router.push('/hishab')} className="btn bg-blue-600 hover:bg-blue-700 focus:ring-blue-500">← Back to Dashboard</button>
      </div>

      <div className="card p-6 mb-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="label">Year</label>
            <input type="number" className="input" value={year} onChange={(e) => setYear(parseInt(e.target.value || "0") || year)} />
          </div>
          <div>
            <label className="label">Month</label>
            <select className="input" value={month} onChange={(e) => setMonth(parseInt(e.target.value))}>
              {Array.from({ length: 12 }, (_, i) => i + 1).map(m => (
                <option key={m} value={m}>{new Date(2000, m - 1, 1).toLocaleString('en-US', { month: 'long' })}</option>
              ))}
            </select>
          </div>
          <div className="flex items-end">
            <button onClick={() => { /* trigger refetch via state change */ setMonth(m => m); }} className="btn">Refresh</button>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="flex items-center justify-center min-h-[200px]"><div className="text-gray-600">Loading report...</div></div>
      ) : report && report.result ? (
        <>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <div className="card p-6">
              <p className="text-sm text-gray-600">Month</p>
              <p className="text-2xl font-bold text-gray-900">{monthName} {report.year}</p>
            </div>
            <div className="card p-6">
              <p className="text-sm text-gray-600">Total Spent</p>
              <p className="text-2xl font-bold text-gray-900">Tk. {report.monthTotal.toFixed(2)}</p>
            </div>
            <div className="card p-6">
              <p className="text-sm text-gray-600">Daily Average</p>
              <p className="text-2xl font-bold text-gray-900">Tk. {report.dailyAverage.toFixed(2)}</p>
            </div>
          </div>

          <div className="card p-6 mb-8">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Top Category</h2>
            <p className="text-gray-800">{report.topCategory || 'N/A'}</p>
          </div>

          <div className="card p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Daily Totals</h2>
            {report.dailyTotals.length === 0 ? (
              <p className="text-gray-600">No data</p>
            ) : (
              <div className="space-y-2">
                {report.dailyTotals.map(row => (
                  <div
                    key={row.date}
                    className="grid grid-cols-3 border-b pb-2 items-start"
                  >
                    <span className="text-gray-800">{row.date}</span>

                    <span className="text-gray-700">
                      {row.itemsWithCost.split("&").map((item, idx, arr) => (
                        <React.Fragment key={idx}>
                          {item.trim()}
                          {idx < arr.length - 1 && <br />}
                        </React.Fragment>
                      ))}
                    </span>

                    <span className="font-semibold text-right">
                      Tk. {row.total.toFixed(2)}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
        </>
      ) : (
        <div className="text-red-600">{report?.message || 'Failed to load report'}</div>
      )}
    </div>
  );
}


