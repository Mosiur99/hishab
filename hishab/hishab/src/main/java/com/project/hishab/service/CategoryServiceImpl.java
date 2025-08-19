package com.project.hishab.service;

import com.project.hishab.entity.Category;
import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CategoryListResponse;
import com.project.hishab.model.CreateCategoryRequest;
import com.project.hishab.repository.CategoryRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl  implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ActionResponse create(CreateCategoryRequest createCategoryRequest) {
        try {
            if (Objects.isNull(createCategoryRequest)) {
                return new ActionResponse(false, "create category request not found");
            }

            if (StringUtils.isBlank(createCategoryRequest.getCategoryType())) {
                return new ActionResponse(false, "category type is required");
            }

            if (createCategoryRequest.getCategoryType().length() > 30) {
                return new ActionResponse(false, "category type size can't be greater to 30 characters");
            }

            Category category = new Category();
            category.setCategoryType(createCategoryRequest.getCategoryType());
            category.setCreated(new Date());
            categoryRepository.save(category);
            return new ActionResponse(true, "create category successfully");
        } catch (Exception e) {
            return new ActionResponse(false, e.getMessage());
        }
    }

    @Override
    public CategoryListResponse list() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                return new CategoryListResponse(true, "No categories found", null);
            }
            
            List<CategoryListResponse.CategoryData> categoryDataList = categories.stream()
                .map(category -> new CategoryListResponse.CategoryData(
                    category.getId(),
                    category.getCategoryType()
                ))
                .collect(Collectors.toList());
            
            return new CategoryListResponse(true, "Categories retrieved successfully", categoryDataList);
        } catch (Exception e) {
            return new CategoryListResponse(false, e.getMessage(), null);
        }
    }
}