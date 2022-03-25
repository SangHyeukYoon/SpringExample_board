package com.yoon.example.springboot.web.dto.users;

import lombok.*;

@Data
public class UserRegistrationDto {

    private String username;
    private String password;
//    private String email;

    @Builder
    UserRegistrationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
