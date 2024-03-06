package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@RequestBody String accessToken, HttpServletResponse response) {
        log.info("{}", accessToken);
        response.addHeader("hi","hi");
        response.addHeader("test","test");
        return ResponseEntity
                .ok()
                .build();
    }
}
