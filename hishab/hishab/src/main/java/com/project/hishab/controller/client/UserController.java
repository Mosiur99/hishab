package com.project.hishab.controller.client;

import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CostSummaryDTO;
import com.project.hishab.model.CreateCostRequest;
import com.project.hishab.model.ExpenseHistoryDTO;
import com.project.hishab.model.ExpenseHistoryResponse;
import com.project.hishab.model.ItemListResponse;
import com.project.hishab.model.MonthlyReportResponse;
import com.project.hishab.model.RecentExpensesResponse;
import com.project.hishab.service.CostService;
import com.project.hishab.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final CostService costService;
    private final ItemService itemService;

    @Autowired
    public UserController(CostService costService, ItemService itemService) {
        this.costService = costService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/cost", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ActionResponse> createCost(@RequestBody CreateCostRequest request,
                                                     HttpServletRequest httpServletRequest) {
        ActionResponse response = costService.createCost(request, httpServletRequest);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ItemListResponse> getAllItems() {
        ItemListResponse response = itemService.list();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/cost-summary", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CostSummaryDTO> getTodayTotalCostByUser(HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(costService.getCostSummaryByUser(httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/expense-history", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExpenseHistoryResponse> getCostsByUserWithFilter(@RequestParam(required = false, name = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                                           @RequestParam(required = false, name = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                                           @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                           HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(costService.getCostsByUserWithFilter(
                    fromDate,
                    toDate,
                    pageNo,
                    pageSize,
                    httpServletRequest
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/monthly-report", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(@RequestParam Integer year,
                                                                  @RequestParam Integer month,
                                                                  HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(costService.getMonthlyReport(year, month, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @RequestMapping(value = "/update-info", method = RequestMethod.POST)
//    public ResponseEntity<>

    @RequestMapping(value = "/recent-expenses", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecentExpensesResponse> getRecentExpenses(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                  HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(costService.getRecentExpenses(pageNo, pageSize, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}























