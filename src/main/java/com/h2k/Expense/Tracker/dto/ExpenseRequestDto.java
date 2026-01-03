package com.h2k.Expense.Tracker.dto;

import com.h2k.Expense.Tracker.entity.type.CategoryType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseRequestDto {
    private String title;
    private BigDecimal amount;
    private CategoryType category;
}
