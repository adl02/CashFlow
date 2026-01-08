package com.h2k.Expense.Tracker.service;

import com.h2k.Expense.Tracker.dto.*;
import com.h2k.Expense.Tracker.entity.Expense;
import com.h2k.Expense.Tracker.entity.User;
import com.h2k.Expense.Tracker.repository.ExpenseRepository;
import com.h2k.Expense.Tracker.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    public ExpenseListResponseDto getAllExpenses() {
        User currentUser = authUtil.getCurrentUser();
        List<ExpenseResponseDto> expenses =
                expenseRepository.findByUserId(currentUser.getId())
                        .stream()
                        .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                        .toList();

        BigDecimal totalIncome = expenseRepository.getTotalIncomeByUser(currentUser.getId());
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUser(currentUser.getId());
        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new ExpenseListResponseDto(totalIncome, totalExpense, balance, expenses);
    }

    public ExpenseResponseDto addExpense(ExpenseRequestDto expenseRequestDto) {
        User currentUser = authUtil.getCurrentUser();
        Expense expense = Expense.builder()
                .title(expenseRequestDto.getTitle())
                .amount(expenseRequestDto.getAmount())
                .expenseDate(LocalDate.now())
                .categoryType(expenseRequestDto.getCategory())
                .transactionType(expenseRequestDto.getTransactionType())
                .user(currentUser)
                .build();

        return modelMapper.map(expenseRepository.save(expense), ExpenseResponseDto.class);
    }

    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto expenseRequestDto) {
        User currentUser = authUtil.getCurrentUser();
        Expense expense = expenseRepository.findByIdAndUserId(id,currentUser.getId()).orElseThrow(() ->
                new IllegalArgumentException("Unauthorized or Expense not found")
        );
        modelMapper.map(expenseRequestDto, expense);
        expense.setModifiedAt(LocalDate.now());
        expense = expenseRepository.save(expense);
        return modelMapper.map(expense, ExpenseResponseDto.class);
    }


    public String deleteExpense(Long id) {
        User currentUser = authUtil.getCurrentUser();
        Expense expense = expenseRepository.findByIdAndUserId(id,currentUser.getId()).orElseThrow(() ->
                new IllegalArgumentException("Unauthorized or Expense not found")
        );
        expenseRepository.delete(expense);
        return "Item deleted successfully";
    }

    public List<CategorySummaryDTO> getByCategories() {
        User currentUser = authUtil.getCurrentUser();
        return expenseRepository.findCategorySummaryByUser(currentUser.getId());
    }

    public MonthlySummaryDTO getMonthlySummary(int month, int year) {
        User currentUser = authUtil.getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        BigDecimal totalExpense = expenseRepository.findTotalMonthlyExpenseByUser(startDate, endDate, currentUser.getId());

        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }

        List<CategorySummaryDTO> categoryBreakdown =
                expenseRepository.findMonthlyCategoryBreakdownByUser(startDate, endDate, currentUser.getId());

        return new MonthlySummaryDTO(
                month,
                year,
                totalExpense,
                categoryBreakdown
        );
    }
}
