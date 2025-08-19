"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { AuthService } from "../services/authService";

export default function AdminDashboardPage() {
  const router = useRouter();

  useEffect(() => {
    if (!AuthService.isAuthenticated()) {
      router.push('/login');
      return;
    }

    const user = AuthService.getUser();
    if (user?.role !== 'ADMIN') {
      router.push('/hishab');
      return;
    }
  }, [router]);

  const handleLogout = async () => {
    await AuthService.logout();
    router.push('/login');
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8 flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
            <p className="mt-2 text-gray-600">Manage the expense tracking system</p>
          </div>
          <div className="flex items-center space-x-4">
            <span className="text-sm text-gray-600">
              Welcome, {AuthService.getUser()?.name} (Admin)
            </span>
            <button
              onClick={handleLogout}
              className="px-4 py-2 text-sm text-red-600 hover:text-red-800 border border-red-300 rounded-md hover:bg-red-50"
            >
              Logout
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-semibold mb-4">User Management</h3>
            <p className="text-gray-600 mb-4">Manage user accounts and permissions</p>
            <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
              Manage Users
            </button>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-semibold mb-4">System Analytics</h3>
            <p className="text-gray-600 mb-4">View system-wide statistics and reports</p>
            <button className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
              View Analytics
            </button>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-semibold mb-4">Database Management</h3>
            <p className="text-gray-600 mb-4">Manage categories, items, and system data</p>
            <button className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700">
              Manage Data
            </button>
          </div>
        </div>

        <div className="mt-8 bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold mb-4">Admin Controls</h2>
          <p className="text-gray-600">
            This is the admin dashboard. Only authenticated users with ADMIN role can access this page.
          </p>
        </div>
      </div>
    </div>
  );
}
