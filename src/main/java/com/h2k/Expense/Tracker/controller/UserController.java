package com.h2k.Expense.Tracker.controller;

import com.h2k.Expense.Tracker.dto.SignupResponseDto;
import com.h2k.Expense.Tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/user")
    public ResponseEntity<List<SignupResponseDto>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

}
