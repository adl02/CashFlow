package com.h2k.Expense.Tracker.entity;

import com.h2k.Expense.Tracker.entity.type.CategoryType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private BigDecimal amount;

    @CreatedDate
    private LocalDate expenseDate;

    @LastModifiedDate
    private LocalDate modifiedAt;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @ManyToOne
    private User user;

}
