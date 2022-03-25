package com.yoon.example.springboot.web.dto.users;

import lombok.Builder;
import lombok.Data;

@Data
public class UserSaveRequestDto {

    private String username;
    private String password;
//    private String email;

    @Builder
    public UserSaveRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
