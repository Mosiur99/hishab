package com.project.hishab.model;

import java.util.List;

public class ItemListResponse {
    
    private boolean result;
    private String message;
    private List<ItemData> data;

    public ItemListResponse(boolean result, String message, List<ItemData> data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public ItemListResponse() {
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

    public List<ItemData> getData() {
        return data;
    }

    public void setData(List<ItemData> data) {
        this.data = data;
    }

    public static class ItemData {
        private Long id;
        private String name;
        private Long categoryId;
        private String categoryType;

        public ItemData(Long id, String name, Long categoryId, String categoryType) {
            this.id = id;
            this.name = name;
            this.categoryId = categoryId;
            this.categoryType = categoryType;
        }

        public ItemData() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public String getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(String categoryType) {
            this.categoryType = categoryType;
        }
    }
}
