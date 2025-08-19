package com.project.hishab.controller.admin;

import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.ItemListResponse;
import com.project.hishab.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemListResponse> getAllItems() {
        ItemListResponse response = itemService.list();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/items/category/{categoryId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemListResponse> getItemsByCategory(@PathVariable Long categoryId) {
        ItemListResponse response = itemService.listByCategory(categoryId);
        return ResponseEntity.ok(response);
    }
}
