package com.h2k.Expense.Tracker.controller;

import com.h2k.Expense.Tracker.dto.LoginRequestDto;
import com.h2k.Expense.Tracker.dto.LoginResponseDto;
import com.h2k.Expense.Tracker.dto.SignupRequestDto;
import com.h2k.Expense.Tracker.dto.SignupResponseDto;
import com.h2k.Expense.Tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> login(@RequestBody SignupRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
//        return ResponseEntity.ok(authService.login(loginRequestDto));
//    }

}
