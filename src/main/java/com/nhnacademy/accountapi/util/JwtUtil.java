package com.nhnacademy.accountapi.util;


import com.nhnacademy.accountapi.properties.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofHours(3).toMillis();
    public static final Long TEST = Duration.ofSeconds(5).toMillis();
    public static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(1).toMillis();
    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String AUTH_HEADER = "Authorization";
    public static final String EXP_HEADER = "X-Expire";
    public static final String TOKEN_TYPE = "Bearer ";
    private final JwtProperties jwtProperties;

    private String createToken(String userId,
                               Collection<? extends GrantedAuthority> authorities,
                               String tokenType,
                               Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(tokenType);
        claims.put("userId", userId);
        claims.put("roles", authorities.toString());

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public String createAccessToken(String userId,
                                    Collection<? extends GrantedAuthority> authorities) {
        return createToken(userId, authorities, ACCESS_TOKEN, TEST);
//        return createToken(userId, authorities, ACCESS_TOKEN, ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(String userId,
                                     Collection<? extends GrantedAuthority> authorities) {
        return createToken(userId, authorities, REFRESH_TOKEN, REFRESH_TOKEN_VALID_TIME);
    }

    public boolean isValidateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes()).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String reCreationAccessToken(Claims claims) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }
}
