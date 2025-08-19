package com.project.hishab.model;

public class CostSummaryDTO {

    private boolean result;
    private String message;
    private Double todayTotal;
    private Double monthTotal;
    private Double dailyAverage;
    private String topCategory;

    public CostSummaryDTO(boolean result,
                          String message,
                          Double todayTotal,
                          Double monthTotal,
                          Double dailyAverage,
                          String topCategory) {
        this.result = result;
        this.message = message;
        this.todayTotal = todayTotal;
        this.monthTotal = monthTotal;
        this.dailyAverage = dailyAverage;
        this.topCategory = topCategory;
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

    public Double getTodayTotal() {
        return todayTotal;
    }

    public void setTodayTotal(Double todayTotal) {
        this.todayTotal = todayTotal;
    }

    public Double getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(Double monthTotal) {
        this.monthTotal = monthTotal;
    }

    public Double getDailyAverage() {
        return dailyAverage;
    }

    public void setDailyAverage(Double dailyAverage) {
        this.dailyAverage = dailyAverage;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }
}
