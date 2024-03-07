package com.nhnacademy.accountapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.accountapi.properties.JwtProperties;
import com.nhnacademy.accountapi.security.details.CustomUserDetailsService;
import com.nhnacademy.accountapi.security.filter.JwtAuthenticationFilter;
import com.nhnacademy.accountapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> sessionRedisTemplate;
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterAt(new JwtAuthenticationFilter(authenticationManager(null),jwtProperties,objectMapper,jwtUtil,sessionRedisTemplate),UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/api/users/**")
            .permitAll()
            .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {

        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){


        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(/*passwordEncoder()*/NoOpPasswordEncoder.getInstance());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService());
        return daoAuthenticationProvider;
    }
}
