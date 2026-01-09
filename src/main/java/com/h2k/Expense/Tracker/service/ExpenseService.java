package com.h2k.Expense.Tracker.service;

import com.h2k.Expense.Tracker.dto.*;
import com.h2k.Expense.Tracker.entity.Expense;
import com.h2k.Expense.Tracker.entity.User;
import com.h2k.Expense.Tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ModelMapper modelMapper;

    @Cacheable(cacheNames = "My-Transactions", key = "#userId")
    public ExpenseListResponseDto getAllExpenses(Long userId) {
        List<ExpenseResponseDto> expenses =
                expenseRepository.findByUserId(userId)
                        .stream()
                        .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                        .toList();

        BigDecimal totalIncome = expenseRepository.getTotalIncomeByUser(userId);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUser(userId);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new ExpenseListResponseDto(totalIncome, totalExpense, balance, expenses);
    }

    @CacheEvict(
            cacheNames = {"My-Transactions", "CategorySummary", "MonthlySummary"},
            allEntries = true
    )    public ExpenseResponseDto addExpense(Long userId, ExpenseRequestDto expenseRequestDto) {
        User user = new User();
        user.setId(userId);
        Expense expense = Expense.builder()
                .title(expenseRequestDto.getTitle())
                .amount(expenseRequestDto.getAmount())
                .expenseDate(LocalDate.now())
                .categoryType(expenseRequestDto.getCategory())
                .transactionType(expenseRequestDto.getTransactionType())
                .user(user)
                .build();

        return modelMapper.map(expenseRepository.save(expense), ExpenseResponseDto.class);
    }

    @CacheEvict(
            cacheNames = {"My-Transactions", "CategorySummary", "MonthlySummary"},
            allEntries = true
    )
    public ExpenseResponseDto updateExpense(Long userId, Long id, ExpenseRequestDto expenseRequestDto) {
        Expense expense = expenseRepository.findByIdAndUserId(id,userId).orElseThrow(() ->
                new IllegalArgumentException("Unauthorized or Expense not found")
        );
        expense.setTitle(expenseRequestDto.getTitle());
        expense.setAmount(expenseRequestDto.getAmount());
        expense.setCategoryType(expenseRequestDto.getCategory());
        expense.setTransactionType(expenseRequestDto.getTransactionType());
        expense.setModifiedAt(LocalDate.now());
        expense = expenseRepository.save(expense);
        return modelMapper.map(expense, ExpenseResponseDto.class);
    }

    @CacheEvict(
            cacheNames = {"My-Transactions", "CategorySummary", "MonthlySummary"},
            allEntries = true
    )
    public String deleteExpense(Long userId, Long id) {
        Expense expense = expenseRepository.findByIdAndUserId(id,userId).orElseThrow(() ->
                new IllegalArgumentException("Unauthorized or Expense not found")
        );
        expenseRepository.delete(expense);
        return "Item deleted successfully";
    }

    @Cacheable(cacheNames = "CategorySummary", key = "#userId")
    public List<CategorySummaryDTO> getByCategories(Long userId) {
        return expenseRepository.findCategorySummaryByUser(userId);
    }

    @Cacheable(
            cacheNames = "MonthlySummary",
            key = "{#userId, #month, #year}"
    )
    public MonthlySummaryDTO getMonthlySummary(Long userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        BigDecimal totalExpense = expenseRepository.findTotalMonthlyExpenseByUser(startDate, endDate, userId);

        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }

        List<CategorySummaryDTO> categoryBreakdown =
                expenseRepository.findMonthlyCategoryBreakdownByUser(startDate, endDate, userId);

        return new MonthlySummaryDTO(
                month,
                year,
                totalExpense,
                categoryBreakdown
        );
    }
}
