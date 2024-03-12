package com.nhnacademy.accountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class AuthUserDto {
    private String userId;
    private String userName;
    private final String userPassword;
    private String userAuth;
}
