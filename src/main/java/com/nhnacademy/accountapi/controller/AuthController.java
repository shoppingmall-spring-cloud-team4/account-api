package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.nhnacademy.accountapi.util.JwtUtil.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> sessionRedisTemplate;
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION,required = false) String accessToken, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshToken(accessToken);
        if (!jwtUtil.isValidateToken(refreshToken)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        Claims claims = jwtUtil.parseClaims(refreshToken);
        String reissueToken = jwtUtil.reCreationAccessToken(claims);
        sessionRedisTemplate.opsForHash().put(REFRESH_TOKEN,reissueToken,refreshToken);

        response.addHeader(AUTH_HEADER,TOKEN_TYPE + reissueToken);
        response.addHeader(EXP_HEADER,String.valueOf(new Date().getTime()+TEST));

        return ResponseEntity
                .ok()
                .build();
    }
}
