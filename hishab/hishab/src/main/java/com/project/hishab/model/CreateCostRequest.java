package com.project.hishab.model;

import com.project.hishab.Enum.PaymentType;
import com.project.hishab.Enum.CostingType;

import java.util.Date;

public class CreateCostRequest {
    
    private Long itemId;
    private Integer quantity;
    private Double perUnitCost;
    private Double totalCost;
    private Double weight;
    private Double perWeightCost;
    private PaymentType paymentType;
    private CostingType costingType;
    private Date costingDate;
    private String description;

    public CreateCostRequest() {
    }

    public CreateCostRequest(Long itemId,
                             Integer quantity,
                             Double perUnitCost,
                             Double totalCost,
                             Double weight,
                             Double perWeightCost,
                             PaymentType paymentType,
                             CostingType costingType,
                             Date costingDate) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.perUnitCost = perUnitCost;
        this.totalCost = totalCost;
        this.weight = weight;
        this.perWeightCost = perWeightCost;
        this.paymentType = paymentType;
        this.costingType = costingType;
        this.costingDate = costingDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public CostingType getCostingType() {
        return costingType;
    }

    public void setCostingType(CostingType costingType) {
        this.costingType = costingType;
    }

    public Date getCostingDate() {
        return costingDate;
    }

    public void setCostingDate(Date costingDate) {
        this.costingDate = costingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
