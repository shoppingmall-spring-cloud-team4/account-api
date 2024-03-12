package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.adaptor.UserAdaptor;
import com.nhnacademy.accountapi.dto.AuthUserDto;
import com.nhnacademy.accountapi.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/account/users")
@RequiredArgsConstructor
public class AccountController {

    private final UserAdaptor userAdaptor;

    @GetMapping
    public ResponseEntity<AuthUserDto> getUser(@RequestHeader("X-USER-ID") String userId, HttpServletRequest request, HttpServletResponse response){
        log.debug("X-USER-ID:{}",userId);
        log.info("X-USER-ID:{}",userId);
        log.info("Authorization:{}",request.getHeader("Authorization"));

        return ResponseEntity
                .ok()
                .body(userAdaptor.getUser(userId));
    }


}
