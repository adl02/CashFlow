package com.h2k.Expense.Tracker.service;

import com.h2k.Expense.Tracker.dto.LoginRequestDto;
import com.h2k.Expense.Tracker.dto.LoginResponseDto;
import com.h2k.Expense.Tracker.dto.SignupRequestDto;
import com.h2k.Expense.Tracker.dto.SignupResponseDto;
import com.h2k.Expense.Tracker.entity.User;
import com.h2k.Expense.Tracker.repository.UserRepository;
import com.h2k.Expense.Tracker.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if (user != null) throw new IllegalArgumentException("User already exists");

        modelMapper.map(signupRequestDto, User.class);

        user = userRepository.save(User.builder()
                .username(signupRequestDto.getUsername())
                .createdAt(LocalDate.now())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build()
        );

        return modelMapper.map(user, SignupResponseDto.class);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDto(token, user.getId());
    }

}
