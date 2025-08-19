package com.project.hishab.model;

import java.util.List;

public class CategoryListResponse {
    
    private boolean result;
    private String message;
    private List<CategoryData> data;

    public CategoryListResponse(boolean result, String message, List<CategoryData> data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public CategoryListResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryData> getData() {
        return data;
    }

    public void setData(List<CategoryData> data) {
        this.data = data;
    }

    public static class CategoryData {
        private Long id;
        private String categoryType;

        public CategoryData(Long id, String categoryType) {
            this.id = id;
            this.categoryType = categoryType;
        }

        public CategoryData() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(String categoryType) {
            this.categoryType = categoryType;
        }
    }
}
