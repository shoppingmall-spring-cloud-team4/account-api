package com.nhnacademy.accountapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.accountapi.properties.JwtProperties;
import com.nhnacademy.accountapi.security.details.CustomUserDetailsService;
import com.nhnacademy.accountapi.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
                //TODO#1-1 - UsernamePasswordAuthenticationFilter를 Custom한 JwtAuthenticationFilter 교체.
            .addFilterAt(new JwtAuthenticationFilter(authenticationManager(null),jwtProperties,objectMapper),UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/api/users/**")
            .permitAll()
            .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //TODO#1-4 authenticationManager를 Bean으로 등록합니다.
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        //TODO#1-2 CustomUserDetailsService를 Bean으로 등록합니다.
        //DaoAuthenticationProvider 사용합니다. 즉 db에서 회원을 조회하는 역할을 합니다.
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        //TODO#1-3 db에서 조회한 회원을 검증하는 provider 입니다.
        //passwordEncoder는 설계한 회원 Table의 password 알고리즘을 고려하여 설정하세요.

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService());
        return daoAuthenticationProvider;
    }
}
