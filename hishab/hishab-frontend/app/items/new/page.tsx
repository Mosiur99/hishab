"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";

type Category = {
  id: number;
  categoryType: string;
};

type CategoryListResponse = {
  result: boolean;
  message: string;
  data: Category[];
};

type CreateItemResponse = {
  result: boolean;
  message: string;
};

export default function CreateItemPage() {
  const router = useRouter();
  const [itemName, setItemName] = useState("");
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoadingCategories, setIsLoadingCategories] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [serverMessage, setServerMessage] = useState<string | null>(null);
  const [serverOk, setServerOk] = useState<boolean | null>(null);

  // Fetch categories on component mount
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch("/api/v1/admin/category/list");
        if (response.ok) {
          const responseData: CategoryListResponse = await response.json();
          if (responseData.result && responseData.data) {
            setCategories(responseData.data);
          } else {
            console.error("Failed to fetch categories:", responseData.message);
          }
        }
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      } finally {
        setIsLoadingCategories(false);
      }
    };

    fetchCategories();
  }, []);

  const isValid = useMemo(() => 
    itemName.trim().length > 0 && selectedCategoryId !== null, 
    [itemName, selectedCategoryId]
  );

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isValid || isSubmitting) return;
    setIsSubmitting(true);
    setServerMessage(null);
    setServerOk(null);
    try {
      const response = await fetch("/api/v1/admin/item/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
          name: itemName, 
          categoryId: selectedCategoryId
        }),
      });

      const data: CreateItemResponse = await response.json();
      setServerMessage(data?.message ?? (response.ok ? "Success" : "Failed"));
      setServerOk(Boolean(data?.result));

      if (data?.result) {
        setItemName("");
        setSelectedCategoryId(null);
      }
    } catch (error) {
      setServerMessage("Network error. Please try again.");
      setServerOk(false);
    } finally {
      setIsSubmitting(false);
    }
  }, [itemName, selectedCategoryId, isSubmitting, isValid]);

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
          <h1 className="text-xl font-semibold">Create Item</h1>
          <p className="mt-1 text-sm text-gray-600">
            Provide the item name and select a category.
          </p>

          <form onSubmit={handleSubmit} className="mt-6 space-y-4">
            <div>
              <label htmlFor="itemName" className="label">Item Name *</label>
              <input
                id="itemName"
                name="itemName"
                className="input"
                placeholder="e.g., Groceries, Rent, Salary"
                value={itemName}
                onChange={(e) => setItemName(e.target.value)}
                autoFocus
                required
              />
              <p className="helper">The name of the item you want to create.</p>
            </div>

            <div>
              <label htmlFor="categorySelect" className="label">Category *</label>
              <select
                id="categorySelect"
                name="categorySelect"
                className="input"
                value={selectedCategoryId || ""}
                onChange={(e) => setSelectedCategoryId(Number(e.target.value) || null)}
                required
                disabled={isLoadingCategories}
              >
                <option value="">Select a category</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.categoryType}
                  </option>
                ))}
              </select>
              <p className="helper">
                {isLoadingCategories ? "Loading categories..." : "Select a category for this item."}
              </p>
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
                {isSubmitting ? "Submitting..." : "Create Item"}
              </button>
              <button
                type="button"
                className="text-sm text-gray-600 hover:text-gray-900"
                onClick={() => {
                  setItemName("");
                  setSelectedCategoryId(null);
                }}
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
