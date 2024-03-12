package com.nhnacademy.accountapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserResponse {

    @JsonProperty("id")
    private String userId;
    @JsonProperty("name")
    private String userName;

    public UserResponse(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
