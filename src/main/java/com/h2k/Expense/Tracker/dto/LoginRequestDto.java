package com.h2k.Expense.Tracker.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}