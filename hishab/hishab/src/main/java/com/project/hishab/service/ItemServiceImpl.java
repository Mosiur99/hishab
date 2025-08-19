package com.project.hishab.service;

import com.project.hishab.entity.Category;
import com.project.hishab.entity.Item;
import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CreateItemRequest;
import com.project.hishab.model.ItemListResponse;
import com.project.hishab.repository.CategoryRepository;
import com.project.hishab.repository.ItemRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ActionResponse create(CreateItemRequest createItemRequest) {
        try {
            if (Objects.isNull(createItemRequest)) {
                return new ActionResponse(false, "create item request not found");
            }

            if (StringUtils.isBlank(createItemRequest.getName())) {
                return new ActionResponse(false, "item name is required");
            }

            if (createItemRequest.getName().length() > 100) {
                return new ActionResponse(false, "item name size can't be greater than 100 characters");
            }

            if (Objects.isNull(createItemRequest.getCategoryId())) {
                return new ActionResponse(false, "category id is required");
            }

            Optional<Category> categoryOptional = categoryRepository.findById(createItemRequest.getCategoryId());
            if (categoryOptional.isEmpty()) {
                return new ActionResponse(false, "category not found with id: " + createItemRequest.getCategoryId());
            }

            Item item = new Item();
            item.setName(createItemRequest.getName());
            item.setCategory(categoryOptional.get());
            item.setCreated(new Date());
            item.setUpdated(new Date());
            itemRepository.save(item);
            return new ActionResponse(true, "create item successfully");
        } catch (Exception e) {
            return new ActionResponse(false, e.getMessage());
        }
    }

    @Override
    public ItemListResponse list() {
        try {
            List<Item> items = itemRepository.findAll();
            if (items.isEmpty()) {
                return new ItemListResponse(true, "No items found", null);
            }
            
            List<ItemListResponse.ItemData> itemDataList = items.stream()
                .map(item -> new ItemListResponse.ItemData(
                    item.getId(),
                    item.getName(),
                    item.getCategory().getId(),
                    item.getCategory().getCategoryType()
                ))
                .collect(Collectors.toList());
            
            return new ItemListResponse(true, "Items retrieved successfully", itemDataList);
        } catch (Exception e) {
            return new ItemListResponse(false, e.getMessage(), null);
        }
    }

    @Override
    public ItemListResponse listByCategory(Long categoryId) {
        try {
            List<Item> items = itemRepository.findByCategoryId(categoryId);
            if (items.isEmpty()) {
                return new ItemListResponse(true, "No items found for category", null);
            }
            
            List<ItemListResponse.ItemData> itemDataList = items.stream()
                .map(item -> new ItemListResponse.ItemData(
                    item.getId(),
                    item.getName(),
                    item.getCategory().getId(),
                    item.getCategory().getCategoryType()
                ))
                .collect(Collectors.toList());
            
            return new ItemListResponse(true, "Items retrieved successfully", itemDataList);
        } catch (Exception e) {
            return new ItemListResponse(false, e.getMessage(), null);
        }
    }
}
