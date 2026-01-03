package com.h2k.Expense.Tracker.controller;

import com.h2k.Expense.Tracker.dto.CategorySummaryDTO;
import com.h2k.Expense.Tracker.dto.ExpenseRequestDto;
import com.h2k.Expense.Tracker.dto.ExpenseResponseDto;
import com.h2k.Expense.Tracker.dto.MonthlySummaryDTO;
import com.h2k.Expense.Tracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;


    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> addExpense(@RequestBody ExpenseRequestDto expenseRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.addExpense(expenseRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDto expenseRequestDto) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expenseRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category-summary")
    public ResponseEntity<List<CategorySummaryDTO>> getByCategories() {
        return ResponseEntity.ok(expenseService.getByCategories());
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryDTO> getMonthlySummary(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(expenseService.getMonthlySummary(month,year));
    }

}
