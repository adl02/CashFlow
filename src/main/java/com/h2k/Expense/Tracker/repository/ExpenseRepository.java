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
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.transactionType = 'EXPENSE'
        AND e.user.id = :userId
    """)
    BigDecimal getTotalExpenseByUser(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.transactionType = 'INCOME'
        AND e.user.id = :userId
    """)
    BigDecimal getTotalIncomeByUser(@Param("userId") Long userId);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    @Query("""
    SELECT new com.h2k.Expense.Tracker.dto.CategorySummaryDTO(
        e.categoryType,
        SUM(e.amount)
    )
    FROM Expense e
    WHERE e.transactionType = 'EXPENSE'
    AND e.user.id = :userId
    GROUP BY e.categoryType
""")
    List<CategorySummaryDTO> findCategorySummaryByUser(
            @Param("userId") Long userId
    );

    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.expenseDate BETWEEN :startDate AND :endDate
    AND e.transactionType = 'EXPENSE'
    AND e.user.id = :userId
""")
    BigDecimal findTotalMonthlyExpenseByUser(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") Long userId
    );

    @Query("""
    SELECT new com.h2k.Expense.Tracker.dto.CategorySummaryDTO(
        e.categoryType,
        SUM(e.amount)
    )
    FROM Expense e
    WHERE e.expenseDate BETWEEN :startDate AND :endDate
    AND e.transactionType = 'EXPENSE'
    AND e.user.id = :userId
    GROUP BY e.categoryType
""")
    List<CategorySummaryDTO> findMonthlyCategoryBreakdownByUser(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") Long userId
    );

}