package com.nhnacademy.accountapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LoginRequest {
    private  String id;
    private  String password;
}