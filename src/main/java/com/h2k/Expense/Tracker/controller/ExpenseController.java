package com.h2k.Expense.Tracker.controller;

import com.h2k.Expense.Tracker.dto.*;
import com.h2k.Expense.Tracker.security.AuthUtil;
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
    private final AuthUtil authUtil;


    @GetMapping
    public ResponseEntity<ExpenseListResponseDto> getAllExpenses() {
        Long userId = authUtil.getCurrentUser().getId();
        return ResponseEntity.ok(expenseService.getAllExpenses(userId));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> addExpense(@RequestBody ExpenseRequestDto expenseRequestDto) {
        Long userId = authUtil.getCurrentUser().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.addExpense(userId,expenseRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDto expenseRequestDto) {
        Long userId = authUtil.getCurrentUser().getId();
        return ResponseEntity.ok(expenseService.updateExpense(userId,id, expenseRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUser().getId();
        String message=expenseService.deleteExpense(userId,id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/category-summary")
    public ResponseEntity<List<CategorySummaryDTO>> getByCategories() {
        Long userId = authUtil.getCurrentUser().getId();
        return ResponseEntity.ok(expenseService.getByCategories(userId));
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryDTO> getMonthlySummary(
            @RequestParam int month,
            @RequestParam int year
    ) {
        Long userId = authUtil.getCurrentUser().getId();
        return ResponseEntity.ok(expenseService.getMonthlySummary(userId,month,year));
    }

}
