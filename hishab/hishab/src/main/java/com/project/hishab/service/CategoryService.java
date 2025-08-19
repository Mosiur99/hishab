package com.project.hishab.service;

import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CategoryListResponse;
import com.project.hishab.model.CreateCategoryRequest;

public interface CategoryService {

    ActionResponse create(CreateCategoryRequest createCategoryRequest);
    
    CategoryListResponse list();
} 