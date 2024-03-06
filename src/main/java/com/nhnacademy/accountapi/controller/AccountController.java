package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.dto.UserResponse;
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
public class AccountController {

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestHeader("X-USER-ID") String userId, HttpServletRequest request, HttpServletResponse response){
        log.debug("X-USER-ID:{}",userId);
        log.info("X-USER-ID:{}",userId);
        log.info("Authorization:{}",request.getHeader("Authorization"));
        //TODO#6 회원조회 API를 구현합니다.
        //UserResponse <-- 각 팀별로 설계한 회원 스키마를 고려하여 수정합니다.
        //X-USER-ID는 Gateway에서 access-token을 검증 후 valid한 token이면 jwt의 payload의 userId를 Header에  X-USER-ID로 추가 합니다.
        //회원은 shoppingmall-api 서버에 회원을 조회할 수 있는 api를 개발<-- 해당 API를 호출 합니다.
//        Cookie cookie = new Cookie("t", "t");
//        cookie.setPath("/");
//        cookie.setMaxAge(259200);
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
//        response.setHeader("Access-Control-Allow-Credentials","true");
//        response.setHeader("withCredentials","true");
        return ResponseEntity
                .ok()
                .body(new UserResponse("nhnacademy", "엔에이치엔아카데미"));
    }
}
