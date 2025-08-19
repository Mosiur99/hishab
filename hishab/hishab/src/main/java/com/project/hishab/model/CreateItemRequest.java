package com.project.hishab.model;

public class CreateItemRequest {
    
    private String name;
    private Long categoryId;

    public CreateItemRequest(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public CreateItemRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
