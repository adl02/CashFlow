package com.h2k.Expense.Tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class MonthlySummaryDTO implements Serializable {
    private int month;
    private int year;

    private BigDecimal totalMonthlyExpense;

    private List<CategorySummaryDTO> categoryBreakdown;
}
