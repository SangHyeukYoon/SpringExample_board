package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.user.UserRepositoryUserDetailsService;
import com.yoon.example.springboot.web.dto.users.UserRegistrationDto;
import com.yoon.example.springboot.web.dto.users.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class RegistrationApiController {

    private final UserRepositoryUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/register")
    public Long registerUser(@RequestBody UserRegistrationDto registrationDto) {
        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .build();

        return userService.registerUser(requestDto);
    }

}
