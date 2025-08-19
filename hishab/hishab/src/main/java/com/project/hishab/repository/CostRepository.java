package com.project.hishab.repository;

import com.project.hishab.entity.Cost;
import com.project.hishab.model.ExpenseHistoryDTO;
import com.project.hishab.model.RecentExpenseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long> {

    @Query("SELECT COALESCE(SUM(c.totalCost), 0) " +
            "FROM Cost c " +
            "WHERE CAST(c.costingDate AS date) = CURRENT_DATE " +
            "AND c.user.id = :userId")
    Double getTodayTotalCostByUser(@Param("userId") Long userId);

    @Query(value = """
    SELECT
        COALESCE(SUM(CASE WHEN DATE(costing_date) = CURRENT_DATE THEN total_cost END), 0) AS today_total,
        
        COALESCE(SUM(CASE WHEN EXTRACT(MONTH FROM costing_date) = EXTRACT(MONTH FROM CURRENT_DATE) 
                           AND EXTRACT(YEAR FROM costing_date) = EXTRACT(YEAR FROM CURRENT_DATE)
                 THEN total_cost END), 0) AS month_total,
        
        COALESCE(SUM(CASE WHEN EXTRACT(MONTH FROM costing_date) = EXTRACT(MONTH FROM CURRENT_DATE) 
                           AND EXTRACT(YEAR FROM costing_date) = EXTRACT(YEAR FROM CURRENT_DATE)
                 THEN total_cost END) / 
                 COUNT(DISTINCT CASE WHEN EXTRACT(MONTH FROM costing_date) = EXTRACT(MONTH FROM CURRENT_DATE)
                                      AND EXTRACT(YEAR FROM costing_date) = EXTRACT(YEAR FROM CURRENT_DATE)
                 THEN DATE(costing_date) END), 0) AS daily_avg,
        
        (SELECT category.category_type 
         FROM cost 
         JOIN category ON cost.category_id = category.id
         WHERE cost.user_id = :userId
         GROUP BY category.id
         ORDER BY SUM(total_cost) DESC
         LIMIT 1) AS top_category
    FROM cost
    WHERE user_id = :userId
    """, nativeQuery = true)
    Object getCostSummaryByUser(@Param("userId") Long userId);

    @Query(value = """
    SELECT new com.project.hishab.model.ExpenseHistoryDTO(
        c.id,
        i.name,
        cat.categoryType,
        c.quantity,
        c.perUnitCost,
        c.totalCost,
        c.weight,
        c.perWeightCost,
        c.paymentType,
        c.costingType,
        c.costingDate,
        c.created
    )
    FROM Cost c
    JOIN c.item i
    JOIN c.category cat
    WHERE c.user.id = :userId
      AND c.costingDate >= COALESCE(:fromDate, c.costingDate)
      AND c.costingDate <= COALESCE(:toDate, c.costingDate)
    ORDER BY c.costingDate DESC
""",
            countQuery = """
    SELECT COUNT(c)
    FROM Cost c
    JOIN c.item i
    JOIN c.category cat
    WHERE c.user.id = :userId
      AND c.costingDate >= COALESCE(:fromDate, c.costingDate)
      AND c.costingDate <= COALESCE(:toDate, c.costingDate)
""")
    Page<ExpenseHistoryDTO> getCostsByUserWithFilter(
            @Param("userId") Long userId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable
    );

    @Query(value = """
    SELECT to_char(costing_date, 'YYYY-MM-DD') as day,
           SUM(total_cost) as total
    FROM cost
    WHERE user_id = :userId
      AND EXTRACT(YEAR FROM costing_date) = :year
      AND EXTRACT(MONTH FROM costing_date) = :month
    GROUP BY day
    ORDER BY day
""", nativeQuery = true)
    List<Object[]> getDailyTotalsForMonth(@Param("userId") Long userId,
                                          @Param("year") int year,
                                          @Param("month") int month);

    @Query(value = """
    SELECT COALESCE(SUM(total_cost), 0)
    FROM cost
    WHERE user_id = :userId
      AND EXTRACT(YEAR FROM costing_date) = :year
      AND EXTRACT(MONTH FROM costing_date) = :month
""", nativeQuery = true)
    Double getMonthTotal(@Param("userId") Long userId,
                         @Param("year") int year,
                         @Param("month") int month);

    @Query(value = """
    SELECT COALESCE(SUM(total_cost), 0) / NULLIF(COUNT(DISTINCT DATE(costing_date)), 0)
    FROM cost
    WHERE user_id = :userId
      AND EXTRACT(YEAR FROM costing_date) = :year
      AND EXTRACT(MONTH FROM costing_date) = :month
""", nativeQuery = true)
    Double getMonthDailyAverage(@Param("userId") Long userId,
                                @Param("year") int year,
                                @Param("month") int month);

    @Query(value = """
    SELECT category.category_type
    FROM cost
    JOIN category ON category.id = cost.category_id
    WHERE cost.user_id = :userId
      AND EXTRACT(YEAR FROM costing_date) = :year
      AND EXTRACT(MONTH FROM costing_date) = :month
    GROUP BY category.category_type
    ORDER BY SUM(total_cost) DESC
    LIMIT 1
""", nativeQuery = true)
    List<String> getTopCategoryForMonth(@Param("userId") Long userId,
                                        @Param("year") int year,
                                        @Param("month") int month);

    @Query(value = """
    SELECT new com.project.hishab.model.RecentExpenseDTO(
        c.id,
        i.name,
        cat.categoryType,
        c.quantity,
        c.perUnitCost,
        c.totalCost,
        c.weight,
        c.perWeightCost,
        c.paymentType,
        c.costingType,
        c.costingDate,
        c.created
    )
    FROM Cost c
    JOIN c.item i
    JOIN c.category cat
    WHERE c.user.id = :userId
    ORDER BY c.created DESC
""",
            countQuery = """
    SELECT COUNT(c)
    FROM Cost c
    JOIN c.item i
    JOIN c.category cat
    WHERE c.user.id = :userId
""")
    Page<RecentExpenseDTO> getRecentExpensesByUser(@Param("userId") Long userId, Pageable pageable);
}
