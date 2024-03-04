package com.nhnacademy.accountapi.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.accountapi.dto.LoginRequest;
import com.nhnacademy.accountapi.dto.TokenResponse;
import com.nhnacademy.accountapi.properties.JwtProperties;
import com.nhnacademy.accountapi.security.details.PrincipalDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter  extends UsernamePasswordAuthenticationFilter  {
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.authenticationManager=authenticationManager;
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtProperties.getExpirationTime() );

        String jwtToken = Jwts.builder()
                .claim("userId", principalDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();

        TokenResponse tokenResponse = new TokenResponse(jwtToken, jwtProperties.getTokenPrefix(), jwtProperties.getExpirationTime());
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenResponse);

        PrintWriter printWriter = response.getWriter();
        printWriter.write(result);
        printWriter.close();

    }
}
