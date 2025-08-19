"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";

type CreateCategoryResponse = {
  result: boolean;
  message: string;
};

export default function CreateCategoryPage() {
  const router = useRouter();
  const [categoryType, setCategoryType] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [serverMessage, setServerMessage] = useState<string | null>(null);
  const [serverOk, setServerOk] = useState<boolean | null>(null);

  const isValid = useMemo(() => categoryType.trim().length > 0, [categoryType]);

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isValid || isSubmitting) return;
    setIsSubmitting(true);
    setServerMessage(null);
    setServerOk(null);
    try {
      const response = await fetch("/api/v1/admin/category/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ categoryType }),
      });

      const data: CreateCategoryResponse = await response.json();
      setServerMessage(data?.message ?? (response.ok ? "Success" : "Failed"));
      setServerOk(Boolean(data?.result));

      if (data?.result) {
        setCategoryType("");
      }
    } catch (error) {
      setServerMessage("Network error. Please try again.");
      setServerOk(false);
    } finally {
      setIsSubmitting(false);
    }
  }, [categoryType, isSubmitting, isValid]);

  return (
    <div className="container-page px-6 py-10">
      <div className="mb-6">
        <button
          type="button"
          onClick={() => router.back()}
          className="text-sm text-gray-600 hover:text-gray-900"
        >
          ← Back
        </button>
      </div>

      <div className="max-w-xl">
        <div className="card border border-gray-100 p-6">
          <h1 className="text-xl font-semibold">Create Category</h1>
          <p className="mt-1 text-sm text-gray-600">
            Provide the category type and submit.
          </p>

          <form onSubmit={handleSubmit} className="mt-6 space-y-4">
            <div>
              <label htmlFor="categoryType" className="label">Category Type</label>
              <input
                id="categoryType"
                name="categoryType"
                className="input"
                placeholder="e.g., Expense, Income"
                value={categoryType}
                onChange={(e) => setCategoryType(e.target.value)}
                autoFocus
              />
              <p className="helper">This is the type string your backend expects.</p>
            </div>

            {serverMessage && (
              <div
                role="status"
                className={
                  serverOk
                    ? "rounded-md bg-green-50 text-green-800 text-sm px-3 py-2 border border-green-200"
                    : "rounded-md bg-red-50 text-red-800 text-sm px-3 py-2 border border-red-200"
                }
              >
                {serverMessage}
              </div>
            )}

            <div className="flex items-center gap-3">
              <button type="submit" className="btn" disabled={!isValid || isSubmitting}>
                {isSubmitting ? "Submitting..." : "Create Category"}
              </button>
              <button
                type="button"
                className="text-sm text-gray-600 hover:text-gray-900"
                onClick={() => setCategoryType("")}
                disabled={isSubmitting}
              >
                Reset
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

