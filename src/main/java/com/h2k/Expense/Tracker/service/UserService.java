package com.h2k.Expense.Tracker.service;

import com.h2k.Expense.Tracker.dto.SignupResponseDto;
import com.h2k.Expense.Tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<SignupResponseDto> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, SignupResponseDto.class))
                .collect(Collectors.toList());
    }
}
