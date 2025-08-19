package com.project.hishab.service;

import com.project.hishab.entity.Cost;
import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CostSummaryDTO;
import com.project.hishab.model.CreateCostRequest;
import com.project.hishab.model.ExpenseHistoryDTO;
import com.project.hishab.model.ExpenseHistoryResponse;
import com.project.hishab.model.MonthlyReportResponse;
import com.project.hishab.model.RecentExpensesResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

public interface CostService {

    ActionResponse createCost(CreateCostRequest request, HttpServletRequest httpServletRequest);

    Cost getCostById(Long id);

    Double getTodayTotalCostByUser(HttpServletRequest httpServletRequest);

    CostSummaryDTO getCostSummaryByUser(HttpServletRequest httpServletRequest);

    ExpenseHistoryResponse getCostsByUserWithFilter(Date fromDate,
                                                    Date toDate,
                                                    Integer pageNo,
                                                    Integer pageSize,
                                                    HttpServletRequest httpServletRequest);

    MonthlyReportResponse getMonthlyReport(int year, int month, HttpServletRequest httpServletRequest);

    RecentExpensesResponse getRecentExpenses(Integer pageNo, Integer pageSize, HttpServletRequest httpServletRequest);
}
