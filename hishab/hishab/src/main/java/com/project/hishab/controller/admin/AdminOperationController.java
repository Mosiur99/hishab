package com.project.hishab.controller.admin;

import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CategoryListResponse;
import com.project.hishab.model.CreateCategoryRequest;
import com.project.hishab.model.CreateItemRequest;
import com.project.hishab.service.CategoryService;
import com.project.hishab.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminOperationController {
    
    private final CategoryService categoryService;
    private final ItemService itemService;

    @Autowired
    public AdminOperationController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }
    
    @PostMapping("/category/create")
    public ResponseEntity<ActionResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        try {
            return ResponseEntity.ok(categoryService.create(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PostMapping("/item/create")
    public ResponseEntity<ActionResponse> createItem(@RequestBody CreateItemRequest request) {
        try {
            return ResponseEntity.ok(itemService.create(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/category/list")
    public ResponseEntity<CategoryListResponse> listCategories() {
        try {
            return ResponseEntity.ok(categoryService.list());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
