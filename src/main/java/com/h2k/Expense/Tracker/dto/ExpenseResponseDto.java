package com.h2k.Expense.Tracker.dto;

import com.h2k.Expense.Tracker.entity.type.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private LocalDate modifiedAt;
    private CategoryType category;

}
