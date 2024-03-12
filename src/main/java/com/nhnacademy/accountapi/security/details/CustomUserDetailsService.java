package com.nhnacademy.accountapi.security.details;

import com.nhnacademy.accountapi.adaptor.UserAdaptor;
import com.nhnacademy.accountapi.domain.User;
import com.nhnacademy.accountapi.dto.AuthUserDto;
import com.nhnacademy.accountapi.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAdaptor userAdaptor;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        AuthUserDto authUserDto = userAdaptor.getUser(userId);
        log.info("{}",userId);
        User user = User.createUser(authUserDto.getUserId(),authUserDto.getUserName(),authUserDto.getUserPassword());
        return new PrincipalDetails(user);
    }

}
