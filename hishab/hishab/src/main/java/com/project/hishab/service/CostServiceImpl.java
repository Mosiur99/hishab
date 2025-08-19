package com.project.hishab.service;

import com.project.hishab.entity.Category;
import com.project.hishab.entity.Cost;
import com.project.hishab.entity.Item;
import com.project.hishab.entity.User;
import com.project.hishab.model.ActionResponse;
import com.project.hishab.model.CostSummaryDTO;
import com.project.hishab.model.CreateCostRequest;
import com.project.hishab.model.ExpenseHistoryDTO;
import com.project.hishab.model.ExpenseHistoryResponse;
import com.project.hishab.model.DailyTotalDTO;
import com.project.hishab.model.MonthlyReportResponse;
import com.project.hishab.model.RecentExpenseDTO;
import com.project.hishab.model.RecentExpensesResponse;
import com.project.hishab.repository.CategoryRepository;
import com.project.hishab.repository.CostRepository;
import com.project.hishab.repository.ItemRepository;
import com.project.hishab.repository.UserRepository;
import com.project.hishab.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CostServiceImpl implements CostService {

    private final CostRepository costRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public CostServiceImpl(CostRepository costRepository,
                           CategoryRepository categoryRepository,
                           ItemRepository itemRepository,
                           UserRepository userRepository,
                           JwtUtil jwtUtil) {
        this.costRepository = costRepository;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ActionResponse createCost(CreateCostRequest request, HttpServletRequest httpServletRequest) {
        try {
            // Validate request
            if (Objects.isNull(request)) {
                return new ActionResponse(false, "Create cost request not found");
            }

            if (Objects.isNull(request.getItemId())) {
                return new ActionResponse(false, "Item ID is required");
            }

            if (Objects.isNull(request.getTotalCost()) || request.getTotalCost() <= 0) {
                return new ActionResponse(false, "Total cost must be greater than 0");
            }

            if (Objects.isNull(request.getPaymentType())) {
                return new ActionResponse(false, "Purchase type is required");
            }

            if (Objects.isNull(request.getCostingType())) {
                return new ActionResponse(false, "Costing type is required");
            }

            if (Objects.isNull(request.getCostingDate())) {
                return new ActionResponse(false, "Costing date is required");
            }

            // Validate costing type specific fields
            if (request.getCostingType().name().equals("QUANTITY")) {
                if (Objects.isNull(request.getQuantity()) || request.getQuantity() <= 0) {
                    return new ActionResponse(false, "Quantity must be greater than 0 for quantity-based costing");
                }
                if (Objects.isNull(request.getPerUnitCost()) || request.getPerUnitCost() <= 0) {
                    return new ActionResponse(false, "Per unit cost must be greater than 0 for quantity-based costing");
                }
            } else if (request.getCostingType().name().equals("WEIGHT")) {
                if (Objects.isNull(request.getWeight()) || request.getWeight() <= 0) {
                    return new ActionResponse(false, "Weight must be greater than 0 for weight-based costing");
                }
                if (Objects.isNull(request.getPerWeightCost()) || request.getPerWeightCost() <= 0) {
                    return new ActionResponse(false, "Per weight cost must be greater than 0 for weight-based costing");
                }
            }

            // Check if item exists and get its category
            Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
            if (itemOpt.isEmpty()) {
                return new ActionResponse(false, "Item not found with ID: " + request.getItemId());
            }

            Item item = itemOpt.get();
            Category category = item.getCategory();

            User user = userRepository.getReferenceById(jwtUtil.extractUserId(jwtUtil.getToken(httpServletRequest)));
            if (Objects.isNull(user)) {
                return new ActionResponse(false, "Account not found");
            }

            // Create cost entity
            Cost cost = new Cost();
            cost.setCategory(category);
            cost.setItem(item);
            cost.setUser(user);
            cost.setQuantity(request.getQuantity());
            cost.setPerUnitCost(request.getPerUnitCost());
            cost.setTotalCost(request.getTotalCost());
            cost.setWeight(request.getWeight());
            cost.setPerWeightCost(request.getPerWeightCost());
            cost.setPaymentType(request.getPaymentType());
            cost.setCostingType(request.getCostingType());
            cost.setCostingDate(request.getCostingDate());
            cost.setCreated(new Date());

            // Save cost
            costRepository.save(cost);

            return new ActionResponse(true, "Cost created successfully");

        } catch (Exception e) {
            return new ActionResponse(false, "Error creating cost: " + e.getMessage());
        }
    }

    @Override
    public Cost getCostById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        return costRepository.findById(id).orElse(null);
    }

    @Override
    public Double getTodayTotalCostByUser(HttpServletRequest httpServletRequest) {
        return costRepository.getTodayTotalCostByUser(jwtUtil.extractUserId(jwtUtil.getToken(httpServletRequest)));
    }

    @Override
    public CostSummaryDTO getCostSummaryByUser(HttpServletRequest httpServletRequest) {
        Object result = costRepository.getCostSummaryByUser(jwtUtil.extractUserId(jwtUtil.getToken(httpServletRequest)));
        Object[] row = (Object[]) result;

        return new CostSummaryDTO(
                true,
                "fetch data successfully",
                row[0] != null ? ((Number) row[0]).doubleValue() : 0.0,
                row[1] != null ? ((Number) row[1]).doubleValue() : 0.0,
                row[2] != null ? ((Number) row[2]).doubleValue() : 0.0,
                (String) row[3]
        );
    }

    @Override
    public ExpenseHistoryResponse getCostsByUserWithFilter(Date fromDate,
                                                            Date toDate,
                                                            Integer pageNo,
                                                            Integer pageSize,
                                                            HttpServletRequest httpServletRequest) {
        int safePageNo = pageNo == null || pageNo < 0 ? 0 : pageNo;
        int safePageSize = pageSize == null || pageSize <= 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(safePageNo, safePageSize, Sort.by("costingDate").descending());
        Page<ExpenseHistoryDTO> page = costRepository.getCostsByUserWithFilter(
                jwtUtil.extractUserId(jwtUtil.getToken(httpServletRequest)),
                fromDate,
                toDate,
                pageable);
        ExpenseHistoryResponse response = new ExpenseHistoryResponse();
        response.setResult(true);
        response.setMessage("Fetched successfully");
        response.setExpenseHistoryDTOs(page.getContent());
        response.setTotalElements(page.getTotalElements());
        response.setPageNo(safePageNo);
        response.setPageSize(safePageSize);
        return response;
    }

    @Override
    public MonthlyReportResponse getMonthlyReport(int year, int month, HttpServletRequest httpServletRequest) {
        Long userId = jwtUtil.extractUserId(jwtUtil.getToken(httpServletRequest));

        Double monthTotal = costRepository.getMonthTotal(userId, year, month);
        Double dailyAverage = costRepository.getMonthDailyAverage(userId, year, month);
        List<String> topCategories = costRepository.getTopCategoryForMonth(userId, year, month);
        String topCategory = topCategories != null && !topCategories.isEmpty() ? topCategories.get(0) : null;

        List<Object[]> rows = costRepository.getDailyTotalsForMonth(userId, year, month);
        List<DailyTotalDTO> dailyTotals = rows.stream()
                .map(r -> new DailyTotalDTO((String) r[0], r[1] != null ? ((Number) r[1]).doubleValue() : 0.0))
                .toList();

        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setResult(true);
        response.setMessage("Fetched successfully");
        response.setYear(year);
        response.setMonth(month);
        response.setMonthTotal(monthTotal != null ? monthTotal : 0.0);
        response.setDailyAverage(dailyAverage != null ? dailyAverage : 0.0);
        response.setTopCategory(topCategory);
        response.setDailyTotals(dailyTotals);
        return response;
    }

    @Override
    public RecentExpensesResponse getRecentExpenses(Integer pageNo, Integer pageSize, HttpServletRequest httpServletRequest) {
        int safePageNo = pageNo == null || pageNo < 0 ? 0 : pageNo;
        int safePageSize = pageSize == null || pageSize <= 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(safePageNo, safePageSize, Sort.by("created").descending());
        
        Page<RecentExpenseDTO> page = costRepository.getRecentExpensesByUser(
                jwtUtil.extractUserId(jwtUtil.getToken(httpServletRequest)),
                pageable);
        
        RecentExpensesResponse response = new RecentExpensesResponse();
        response.setResult(true);
        response.setMessage("Fetched successfully");
        response.setRecentExpenses(page.getContent());
        response.setTotalElements(page.getTotalElements());
        response.setPageNo(safePageNo);
        response.setPageSize(safePageSize);
        return response;
    }
}
