package com.h2k.Expense.Tracker.service;

import com.h2k.Expense.Tracker.dto.CategorySummaryDTO;
import com.h2k.Expense.Tracker.dto.ExpenseRequestDto;
import com.h2k.Expense.Tracker.dto.ExpenseResponseDto;
import com.h2k.Expense.Tracker.dto.MonthlySummaryDTO;
import com.h2k.Expense.Tracker.entity.Expense;
import com.h2k.Expense.Tracker.repository.ExpenseRepository;
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

    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                .toList();
    }

    public ExpenseResponseDto addExpense(ExpenseRequestDto expenseRequestDto) {
        Expense expense = Expense.builder()
                .title(expenseRequestDto.getTitle())
                .amount(expenseRequestDto.getAmount())
                .expenseDate(LocalDate.now())
                .categoryType(expenseRequestDto.getCategory())
                .build();

        return modelMapper.map(expenseRepository.save(expense), ExpenseResponseDto.class);
    }

    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto expenseRequestDto) {
        Expense expense = expenseRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Expenses not found with this id : "+id));
        modelMapper.map(expenseRequestDto, expense);
        expense.setModifiedAt(LocalDate.now());
        expense = expenseRepository.save(expense);
        return modelMapper.map(expense, ExpenseResponseDto.class);
    }


    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Expenses not found with this id : "+id));
        expenseRepository.deleteById(id);
    }

    public List<CategorySummaryDTO> getByCategories() {
        return expenseRepository.findCategorySummary();
    }

    public MonthlySummaryDTO getMonthlySummary(int month, int year) {

        LocalDate startDate = LocalDate.of(year, month,1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        BigDecimal totalExpense = expenseRepository.findTotalMonthlyExpense(startDate, endDate);

        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }

        List<CategorySummaryDTO> categoryBreakdown =
                expenseRepository.findMonthlyCategoryBreakdown(startDate, endDate);

        return new MonthlySummaryDTO(
                month,
                year,
                totalExpense,
                categoryBreakdown
        );
    }
}
