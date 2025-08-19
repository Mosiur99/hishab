package com.project.hishab.model;

import com.project.hishab.Enum.CostingType;
import com.project.hishab.Enum.PaymentType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseHistoryDTO {

    private Long id;
    private String itemName;
    private String categoryType;
    private Integer quantity;
    private Double perUnitCost;
    private Double totalCost;
    private Double weight;
    private Double perWeightCost;
    private String paymentType;
    private String costingType;
    private String costingDate;
    private String created;

    public ExpenseHistoryDTO(Long id,
                             String itemName,
                             String categoryType,
                             Integer quantity,
                             Double perUnitCost,
                             Double totalCost,
                             Double weight,
                             Double perWeightCost,
                             PaymentType paymentType,
                             CostingType costingType,
                             Date costingDate,
                             Date created) {
        this.id = id;
        this.itemName = itemName;
        this.categoryType = categoryType;
        this.quantity = quantity;
        this.perUnitCost = perUnitCost;
        this.totalCost = totalCost;
        this.weight = weight;
        this.perWeightCost = perWeightCost;
        this.paymentType = paymentType.name();
        this.costingType = costingType.name();
        this.costingDate = new SimpleDateFormat("yyyy-MM-dd").format(costingDate);
        this.created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(created);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPerUnitCost() {
        return perUnitCost;
    }

    public void setPerUnitCost(Double perUnitCost) {
        this.perUnitCost = perUnitCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPerWeightCost() {
        return perWeightCost;
    }

    public void setPerWeightCost(Double perWeightCost) {
        this.perWeightCost = perWeightCost;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCostingType() {
        return costingType;
    }

    public void setCostingType(String costingType) {
        this.costingType = costingType;
    }

    public String getCostingDate() {
        return costingDate;
    }

    public void setCostingDate(String costingDate) {
        this.costingDate = costingDate;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
