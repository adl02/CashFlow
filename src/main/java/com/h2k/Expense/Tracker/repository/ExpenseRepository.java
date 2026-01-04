package com.h2k.Expense.Tracker.repository;

import com.h2k.Expense.Tracker.dto.CategorySummaryDTO;
import com.h2k.Expense.Tracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e.categoryType AS categoryType, SUM(e.amount) AS totalAmount " +
            "FROM Expense e GROUP BY e.categoryType"
    )
    List<CategorySummaryDTO> findCategorySummary();

    @Query("SELECT SUM(e.amount) FROM Expense e " +
            "WHERE e.expenseDate BETWEEN :startDate " +
            "AND :endDate")
    BigDecimal findTotalMonthlyExpense(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT new com.h2k.Expense.Tracker.dto.CategorySummaryDTO(
                e.categoryType,
                SUM(e.amount)
            )
            FROM Expense e
            WHERE e.expenseDate BETWEEN :startDate AND :endDate
            GROUP BY e.categoryType
            """)
    List<CategorySummaryDTO> findMonthlyCategoryBreakdown(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}