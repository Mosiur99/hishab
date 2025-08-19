package com.project.hishab.model;

import java.util.List;

public class ExpenseHistoryResponse {

    private boolean result;
    private String message;
    private List<ExpenseHistoryDTO> expenseHistoryDTOs;
    private Long totalElements;
    private Integer pageNo;
    private Integer pageSize;

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

    public List<ExpenseHistoryDTO> getExpenseHistoryDTOs() {
        return expenseHistoryDTOs;
    }

    public void setExpenseHistoryDTOs(List<ExpenseHistoryDTO> expenseHistoryDTOs) {
        this.expenseHistoryDTOs = expenseHistoryDTOs;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
