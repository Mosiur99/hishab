"use client";

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { AuthService } from './services/authService';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    if (AuthService.isAuthenticated()) {
      const user = AuthService.getUser();
      if (user?.role === 'ADMIN') {
        router.push('/admin');
      } else {
        router.push('/hishab');
      }
    } else {
      router.push('/login');
    }
  }, [router]);

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-2xl font-semibold text-gray-900 mb-2">Hishab</h1>
        <p className="text-gray-600">Redirecting...</p>
      </div>
    </div>
  );
}
