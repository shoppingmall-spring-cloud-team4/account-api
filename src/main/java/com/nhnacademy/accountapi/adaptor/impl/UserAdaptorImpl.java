package com.nhnacademy.accountapi.adaptor.impl;

import com.nhnacademy.accountapi.adaptor.UserAdaptor;
import com.nhnacademy.accountapi.dto.AuthUserDto;
import com.nhnacademy.accountapi.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAdaptorImpl implements UserAdaptor {
    private final RestTemplate restTemplate;

    @Override
    public AuthUserDto getUser(String userId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<AuthUserDto> userResponse = restTemplate.exchange(
                "http://localhost:8000/api/shop/user/{userId}",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                },
                userId
        );
        return userResponse.getBody();
    }
}
