package com.nhnacademy.accountapi.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.accountapi.dto.LoginRequest;
import com.nhnacademy.accountapi.dto.TokenResponse;
import com.nhnacademy.accountapi.properties.JwtProperties;
import com.nhnacademy.accountapi.security.details.PrincipalDetails;
import com.nhnacademy.accountapi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static com.nhnacademy.accountapi.util.JwtUtil.*;

@Slf4j
public class JwtAuthenticationFilter  extends UsernamePasswordAuthenticationFilter  {
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> sessionRedisTemplate;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtProperties jwtProperties,
                                   ObjectMapper objectMapper,
                                   JwtUtil jwtUtil,
                                   RedisTemplate<String, Object> sessionRedisTemplate) {
        super(authenticationManager);
        this.authenticationManager=authenticationManager;
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.sessionRedisTemplate = sessionRedisTemplate;
        //TODO#2-login url은 /login으로 설정합니다. 변경은 jwtProperties를 참고 하세요.
        setFilterProcessesUrl(jwtProperties.getLoginUrl());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = null;
        try {
            //TODO#3 - LoginRequest dto에 id , password를 바인딩 합니다.
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("JwtAuthenticationFilter : {}", loginRequest);
        log.info("JwtAuthenticationFilter : {}", loginRequest);
        //TODO3-usernamePasswordAuthenticationToken객체를 생성하고 authenticationManager에게 parameter로 전달 후 회원이 있는지 검증합니다.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getId(),loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //TODO#5 인증이 성공 하면 JWT 를 발급 합니다.
        //발급된 jwt 는 base64로 encoding 됩니다.

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(principalDetails.getUsername(), principalDetails.getAuthorities());
        String refreshToken = jwtUtil.createRefreshToken(principalDetails.getUsername(), principalDetails.getAuthorities());

        sessionRedisTemplate.opsForHash().put(REFRESH_TOKEN,accessToken,refreshToken);

        TokenResponse tokenResponse = new TokenResponse(accessToken,refreshToken, jwtProperties.getTokenPrefix(), jwtProperties.getExpirationTime());
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenResponse);
        response.addHeader(AUTH_HEADER,TOKEN_TYPE + accessToken);
        response.addHeader(EXP_HEADER,String.valueOf(new Date().getTime()+TEST));

    }

}
