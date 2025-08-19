package com.project.hishab.service;

import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CreateItemRequest;
import com.project.hishab.model.ItemListResponse;

public interface ItemService {

    ActionResponse create(CreateItemRequest createItemRequest);
    
    ItemListResponse list();
    ItemListResponse listByCategory(Long categoryId);
}
