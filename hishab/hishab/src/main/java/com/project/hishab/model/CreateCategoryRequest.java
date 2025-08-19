package com.project.hishab.model;

public class CreateCategoryRequest {
    
    private String categoryType;

    public CreateCategoryRequest(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}