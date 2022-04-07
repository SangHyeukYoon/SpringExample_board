package com.yoon.example.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "사용자"),
    NOT_ENROLLED("ROLE_NOT_ENROLLED", "미등록 사용자");

    private final String key;
    private final String title;

}
